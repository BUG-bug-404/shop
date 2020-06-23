package com.keith.modules.service.user.impl;

import cn.licoy.encryptbody.util.MD5EncryptUtil;
import com.alibaba.fastjson.JSON;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.util.StringUtil;
import com.core.utils.RandomUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.keith.common.exception.RRException;
import com.keith.common.utils.DateUtils;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;
import com.keith.modules.dao.user.UserMemberDao;
import com.keith.modules.dto.PageDTO;
import com.keith.modules.dto.user.AddUserDTO;
import com.keith.modules.dto.user.UpdateUserDTO;
import com.keith.modules.dto.user.UserForgetDTO;
import com.keith.modules.entity.order.OrderOrder;
import com.keith.modules.entity.record.StoreRecord;
import com.keith.modules.entity.sub.SubAccountPay;
import com.keith.modules.entity.token.TokenEntity;
import com.keith.modules.entity.user.UserAcountHistory;
import com.keith.modules.entity.user.UserLevelChange;
import com.keith.modules.entity.user.UserMember;
import com.keith.modules.service.order.OrderOrderService;
import com.keith.modules.service.record.StoreRecordService;
import com.keith.modules.service.sub.SubAccountPayService;
import com.keith.modules.service.token.TokenService;
import com.keith.modules.service.upload.UploadService;
import com.keith.modules.service.user.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

import static com.keith.common.utils.AES.wxDecrypt;


@Service("userMemberService")
public class UserMemberServiceImpl extends ServiceImpl<UserMemberDao, UserMember> implements UserMemberService {

    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private UserLevelChangeService userLevelChangeService;


    @Autowired
    private UserBanlanceService userBanlanceService;

    @Autowired
    private UserAcountHistoryService userAcountHistoryService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SubAccountPayService subAccountPayService;

    @Autowired
    private  UserMemberDao userMemberDao;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private StoreRecordService storeRecordService;

    @Autowired
    private OrderOrderService orderOrderService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserMember> page = this.page(
                new Query<UserMember>().getPage(params),
                new QueryWrapper<UserMember>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils getPage(PageDTO pageDTO){
        IPage<UserMember> page = new Page<>(pageDTO.getPage(),pageDTO.getPageSize());
        page = this.baseMapper.selectPage(page,new QueryWrapper<>());
        return new PageUtils(page);
    }
    @Override
    public List<UserMember> All() {
        System.out.println("wozhixingle");
        return this.list(new QueryWrapper<UserMember>());
    }

    @Override
    public UserMember login(Long userId){
        UserMember userMember = this.getOne(new QueryWrapper<UserMember>().eq("id",userId));
        return userMember;
    }

    @Override
    public void updatePassword(UpdateUserDTO user,Long userId){

        QueryWrapper<UserMember> queryWrapper = new QueryWrapper<>();
        UserMember userMember = this.getOne(queryWrapper.eq("id",userId));;
        if(userMember == null){
            throw new RRException("该账号不存在或异常，请重新尝试！");
        }
        if(!user.getPaymentCode().equals(userMember.getPaymentCode())) {
            throw new RRException("旧密码填写错误！");
        }
        userMember.setPaymentCode(user.getNewPaymentCode());
        UpdateWrapper<UserMember> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("payment_code",user.getNewPaymentCode()).eq("id",userId);
        try {
            baseMapper.updateById(userMember);
        } catch (Exception e) {
            throw new RRException("更新密码失败！"+e.getMessage());
        }

    }

    /**
     * 对微信返回的手机加密信息解密
     * @param encrypted
     * @param session_key
     * @param iv
     * @return
     * @throws IOException
     */
    public String decodeUserInfo(String encrypted, String session_key, String iv) throws IOException {

        String json = wxDecrypt(encrypted, session_key, iv);
        System.out.println(json);
        return json;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertWx(AddUserDTO addUserDTO,Long userId) throws  IOException{
        //根据TOKEN拿用户ID
//        TokenEntity tokenEntity = tokenService.queryByToken(token);
        UserMember userMember = new UserMember();
//        userMember.setId(tokenEntity.getUserId());
        userMember.setId(userId);
        String  mobile = "";
        //判断是否有手机信息
        if(StringUtils.isNotEmpty(addUserDTO.getEncryptedData())&&StringUtils.isNotEmpty(addUserDTO.getIv())){
            //查询sessionKey
            UserMember userMember1 = this.baseMapper.selectOne(new QueryWrapper<UserMember>().eq("id",userId));
            String  result = decodeUserInfo(addUserDTO.getEncryptedData(),userMember1.getWxSessionKey(),addUserDTO.getIv());
            mobile = JSON.parseObject(result).getString("phoneNumber");
                System.out.println("phone::::::::"+mobile);
            addUserDTO.setMobile(mobile);
        }
        //查看邀请人
        UserMember inviter = this.baseMapper.selectOne(new QueryWrapper<UserMember>().eq("invite_code", addUserDTO.getInviteCode()));
        if (inviter != null){
            if (!"".equals(inviter.getId()) && inviter.getId() != null) {
                userMember.setParentId(inviter.getParentId());
                userMember.setParentName(inviter.getUsername());
            }
        BeanUtils.copyProperties(addUserDTO, userMember);
            userMember.setMobile(mobile);

        try {
            this.baseMapper.updateById(userMember);
//            this.baseMapper.insert(userMember);
            //插入用户等级变更表，默认为普通
            UserLevelChange userLevelChange = new UserLevelChange();
            userLevelChange.setUserMemberId(userMember.getId());
            userLevelChange.setLevelId(1L);
            userLevelChange.setWelfareStatus(0);
            userLevelChange.setCreateTime(new Date());
            //判断此用户是否存在
            userLevelChangeService.save(userLevelChange);
        } catch (Exception e) {
            throw new RRException("用户保存失败", e);
        }
    }else{
            BeanUtils.copyProperties(addUserDTO, userMember);
            try {
//                this.baseMapper.insert(userMember);
                this.baseMapper.updateById(userMember);
                //插入用户等级变更表，默认为普通
                UserLevelChange userLevelChange = new UserLevelChange();
                userLevelChange.setUserMemberId(userMember.getId());
                userLevelChange.setLevelId(1L);
                userLevelChange.setWelfareStatus(0);
                userLevelChange.setCreateTime(new Date());
                userLevelChangeService.save(userLevelChange);
            } catch (Exception e) {
                throw new RRException("用户保存失败", e);
            }
        }
    }
    @Override
    public   void updateNc(UpdateUserDTO updateUserDTO,Long userId){
        UserMember userMember = this.getOne(new QueryWrapper<UserMember>().eq("id",userId));
        if(userMember == null ){
            throw  new RRException("该用户不存在");
        }
        UserMember user = new UserMember();
        user.setUsername(updateUserDTO.getUsername());
        user.setId(userMember.getId());
        //同时修改下级人员中保存的用户名
        List<UserMember> sub = this.baseMapper.selectList(new QueryWrapper<UserMember>().eq("parent_id",userId));
        try {
            this.baseMapper.updateById(user);
            if(sub.size()>0){
                for (UserMember userMember1: sub){
                    userMember1.setParentName(updateUserDTO.getUsername());
                    this.baseMapper.updateById(userMember1);
                }
            }

        } catch (Exception e) {
            throw new RRException("保存昵称失败！",e);
        }
    }

    @Override
    public void setPayCode(UpdateUserDTO updateUserDTO,Long userId){
        UserMember userMember = this.getOne(new QueryWrapper<UserMember>().eq("id",userId));
        if(userMember == null ){
            throw  new RRException("该用户不存在");
        }

        UserMember user = new UserMember();
        String paymentCode = MD5EncryptUtil.encrypt(updateUserDTO.getPaymentCode() + userMember.getMobile());
//        user.setPaymentCode(updateUserDTO.getPaymentCode());
        user.setPaymentCode(paymentCode);
        user.setId(userMember.getId());
        try {
            this.baseMapper.updateById(user);
        } catch (Exception e) {
            throw new RRException("支付密码",e);
        }
    }
    /**
     * 支付密码验证
     */
    @Override
    public Boolean validatePayCode(UpdateUserDTO updateUserDTO,Long userId){
        UserMember userMember = this.baseMapper.selectOne(new QueryWrapper<UserMember>().eq("id",userId));
        if(userMember == null){
            throw new RRException("该用户不存在！");
        }
        if(StringUtils.isEmpty(userMember.getPaymentCode())){
            throw new RRException("该用户暂未设置支付密码！");
        }
        String paymentCode = MD5EncryptUtil.encrypt(updateUserDTO.getPaymentCode() + userMember.getMobile());
        if(paymentCode.equals(userMember.getPaymentCode())){
            return true;
        }else {
            return false;
        }
    }
    /**
     * 判断是否有支付密码
     */
    @Override
    public Boolean validateExsit(Long userId){
        UserMember userMember = this.baseMapper.selectOne(new QueryWrapper<UserMember>().eq("id",userId));
        if(StringUtil.isEmpty(userMember.getPaymentCode())){
            return false;
        }
        return true;
    }
    @Override
    public UserMember getInfo(Long userId){
        UserMember userMember = this.getOne(new QueryWrapper<UserMember>().eq("id",userId));
        return userMember;
    }
    /**
     * 修改登录密码
     */
    @Override
    public void updateLoginPassword(UpdateUserDTO updateUserDTO,Long userId){
        UserMember userMember = this.getOne(new QueryWrapper<UserMember>().eq("id",userId));
        if(userMember == null ){
            throw  new RRException("该用户不存在");
        }
        String password = MD5EncryptUtil.encrypt(updateUserDTO.getPassword() + userMember.getMobile());
        if(StringUtils.isNotEmpty(userMember.getPassword())){
            if(!password.equals(userMember.getPassword())){
                throw new RRException("旧密码错误！");
            }
        }
        UserMember user = new UserMember();
        String newPassword = MD5EncryptUtil.encrypt(updateUserDTO.getNewPassword() + userMember.getMobile());
        user.setPassword(newPassword);
        user.setId(userMember.getId());
        try {
            this.baseMapper.updateById(user);
        } catch (Exception e) {
            throw new RRException("登录密码设置失败",e);
        }
    }

    @Override
    public void forgetLoginPassword(UserForgetDTO userForgetDTO,Long userId){
        //验证短信验证码
        Object code = redisTemplate.opsForValue().get("smsCode"+userForgetDTO.getMobile()+userForgetDTO.getSmsType());
        if("".equals(userForgetDTO.getSmsCode())||userForgetDTO.getSmsCode() == null){
            throw new RRException("验证码不能为空！");
        }else if(code == null||"".equals(code)){
            throw new RRException("验证码已失效，请重新再试！");
        }else if(!code.equals(userForgetDTO.getSmsCode())){
            throw new RRException("验证码错误！");
        }
        UserMember userMember = this.getOne(new UpdateWrapper<UserMember>().eq("id",userId));
        if(userMember == null ){
            throw new RRException("账户资源不存在！");
        }
        String password = MD5EncryptUtil.encrypt(userForgetDTO.getNewPassword() + userMember.getMobile());
        userMember.setPassword(password);
        try {
            this.baseMapper.updateById(userMember);
        } catch (Exception e) {
            throw  new RRException("修改密码失败！",e);
        }

    }
    /**
     * 忘记支付密码
     */
    @Override
    public void forgetPayPassword(UserForgetDTO userForgetDTO,Long userId){
        //验证短信验证码
        Object code = redisTemplate.opsForValue().get("smsCode"+userForgetDTO.getMobile()+userForgetDTO.getSmsType());
        if("".equals(userForgetDTO.getSmsCode())||userForgetDTO.getSmsCode() == null){
            throw new RRException("验证码不能为空！");
        }else if(code == null||"".equals(code)){
            throw new RRException("验证码已失效，请重新再试！");
        }else if(!code.equals(userForgetDTO.getSmsCode())){
            throw new RRException("验证码错误！");
        }
        UserMember userMember = this.getOne(new UpdateWrapper<UserMember>().eq("id",userId));
        if(userMember == null ){
            throw new RRException("账户资源不存在！");
        }
        String password = MD5EncryptUtil.encrypt(userForgetDTO.getNewPassword() + userMember.getMobile());
        userMember.setPaymentCode(password);
        try {
            this.baseMapper.updateById(userMember);
        } catch (Exception e) {
            throw  new RRException("修改支付密码失败！",e);
        }
    }


    @Override
    public String inviteCode(Long userId){
        UserMember userMember = this.baseMapper.selectOne(new QueryWrapper<UserMember>().eq("id",userId));
        //如果已存在邀请码直接返回，不存在的话则根据ID生成
        String code = "";
        if(!"".equals(userMember.getInviteCode()) && userMember.getInviteCode() != null){
            return userMember.getInviteCode();
        }else{
            code = this.getCode(userMember.getId());
            //插入数据库保存(先查询是否有一致的)
            userMember.setInviteCode(code);
            this.baseMapper.updateById(userMember);
            return code;
        }
    }
    //根据Id生成邀请码
    public String  getCode(Long id){
        String code = "";
        if(String.valueOf(id).length()<8){
            code = RandomUtil.generateNumberString((8-String.valueOf(id).length()));
        }else{
            code = String.valueOf(id);
        }
        //查询数据库是否有一样的
        boolean flag = true;
        while (flag){
            List<UserMember> list = this.baseMapper.selectList(new QueryWrapper<UserMember>().eq("invite_code",code));
            if(list == null || list.size()==0){
                flag = false;
            }
        }
        return code;
    }





    /**
     * 更新用户邀请的A级店长人数同时判断是否达到自身等级提升条件
     * 成为A级店主条件：成功邀请A级店主>3人，且成功分享3单（仅自卖，不含自买，且3单为不同人员）
     * 成为B级店主条件：直接邀请A级店主>10人，且总社群人数>20人（直邀+间邀）
     * 成为C级店主条件：直接邀请A级店主>20人，且社群总数>200人（直邀+间邀），且直属B级团长>4个
     */
    @Override
    public void updateCount(Long id){
        UserMember user = this.baseMapper.selectById(id);
        try {
            if(user != null){
                Integer num = user.getInviteCount();
                user.setInviteCount(num+1);
                this.baseMapper.updateById(user);
            }
            //修改用户直邀A级店长数的时候查看是否满足店长等级提升
        } catch (Exception e) {
//            throw new RRException("操作失败！",e);
        }
    }


    /**
     * 获取该用户的上级用户
     */
    @Override
    public UserMember getParentInfo(Long id){
        UserMember userMember = this.baseMapper.selectOne(new QueryWrapper<UserMember>().eq("id",id));
        if(userMember == null){
           return null;
        }
        return this.baseMapper.selectOne(new QueryWrapper<UserMember>().eq("id",userMember.getParentId()));
    }


    /**
     * 遍历所有没有上一级的用户,进行一层层遍历，每一个都进行分佣处理，直到没有上一级
     * @param
     */
    @Override
    public void forNoP() throws Exception{
        List<UserMember> list = this.noSubUser();
        for(UserMember user:list){
            testParent(user);
        }
    }
    /**
     * 测试遍历上一级
     */
    public void testParent(UserMember a) throws Exception{
        System.out.println("计算当前用户的分佣："+a.getId()+"::"+a.getUsername());
        this.calculateForMonth(a);
        UserMember userMember = this.getParentInfo(a.getId());
        if(userMember!=null){
            testParent(userMember);
        }
    }

    /**
     * 以当前店主计算当前店主的上级分佣
     * 遍历所有没有下级的用户，查出该用户的上级和上级的上级按照店家等级进行分佣
     * A级店主权益  1.平台对应会员价
     *            2.新人福利特价商品，5选1
     *            3.享受邀请直邀店主孵化奖69元
     *            4.享受社群内每月佣金8%，作为管理服务费
     * B级店主权益  1.自购返利10%
     *            2.享受9折平台价格（铺货）
     *            3.新人福利特价商品，8选2
     *            4.社群内每月佣金额10%，作为管理服务费
     *            5.同级孵化店主社群内没有佣金额3%，作为推荐孵化奖励
     * C级店主权益  1.自购返利13%
     *            2.享受8.5折平台价格（铺货）
     *            3.社群内每月佣金额10%，作为管理服务费
     *            4.社群内直邀团长每月佣金额的15%,作为管理服务费
     *            5.社群内间邀团长每月佣金额的15%，作为管理服务费
     *            6.同级孵化店主社群内没有佣金额3%，作为推荐孵化奖励
     * @param
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void calculateForMonth(UserMember user) throws Exception{

        //本次结算时间
        Date startTime = DateUtils.getBeforeMonthLastDay();
        Date endTime = DateUtils.getThisMonthLastDay();
        //当前年月Date
        Date ym = this.getYearMonth();
        //获取当前用户的分账记录，统计当前结算月的分账总额
        List<SubAccountPay> subAccountPays = subAccountPayService.list(
                new QueryWrapper<SubAccountPay>().eq("shop_id",user.getId())
                        .eq("pay_status",2).ge("pay_time",startTime).le("pay_time",endTime));
        BigDecimal account = this.monthSubAccount(subAccountPays);
        //若当前店主登记为B级或者C级时，需要计算只要团长和间邀团长分佣，还有社群内统计孵化分佣

        //上级店主
        UserMember upUser = this.baseMapper.selectOne(new QueryWrapper<UserMember>().eq("id",user.getParentId()));
        if(upUser != null) {
            //判断店主升级时间是否在本月内，如是则以升级时间为节点，计算升级前与升级后的分佣比
            UserLevelChange userLevelChange = userLevelChangeService.getOne(
                    new QueryWrapper<UserLevelChange>().eq("user_member_id",upUser.getId())
                            .orderByDesc("create_time").last("LIMIT 1"));

            Date firstDay = getmindate();

            if (upUser.getLevelId() == 2) {//A级店主

                BigDecimal accountA = new BigDecimal(0);
                if(userLevelChange.getCreateTime().getTime()>firstDay.getTime()){
                    List<SubAccountPay> subAccountPays1 = subAccountPayService.list(
                            new QueryWrapper<SubAccountPay>().eq("shop_id",user.getId())
                                    .eq("pay_status",2).ge("pay_time",userLevelChange.getCreateTime()).le("pay_time",endTime));
                    account = this.monthSubAccount(subAccountPays1);
                    accountA = account.multiply(new BigDecimal(0.08)).setScale(2,BigDecimal.ROUND_HALF_UP);
                }else{
                     accountA = account.multiply(new BigDecimal(0.08)).setScale(2,BigDecimal.ROUND_HALF_UP);
                }
                UserAcountHistory history = new UserAcountHistory();
                history.setUserMemberId(upUser.getId());
                history.setNote("来自社群粉丝" + this.getYearMonthStr() + "的月分佣。");
                history.setBalanceHistory(accountA);
                history.setCreateTime(ym);
                history.setBalanceType(0);
                try {
                    userAcountHistoryService.save(history);
                    //更新到用户钱包
                    userBanlanceService.updateBanlance(upUser.getId(), accountA);
                } catch (Exception e) {
                    throw new RRException("分佣异常！"+e);
                }
            } else if (upUser.getLevelId() == 3) {//B级店主
                BigDecimal accountB = new BigDecimal(0);
                BigDecimal beginAccount = new BigDecimal(0);
                BigDecimal endAccount = new BigDecimal(0);
                if(userLevelChange.getCreateTime().getTime()>firstDay.getTime()){
                    List<SubAccountPay> begin = subAccountPayService.list(
                            new QueryWrapper<SubAccountPay>().eq("shop_id",user.getId())
                                    .eq("pay_status",2).ge("pay_time",startTime).le("pay_time",userLevelChange.getCreateTime()));
                    List<SubAccountPay> end = subAccountPayService.list(
                            new QueryWrapper<SubAccountPay>().eq("shop_id",user.getId())
                                    .eq("pay_status",2).ge("pay_time",userLevelChange.getCreateTime()).le("pay_time",endTime));
                     beginAccount = this.monthSubAccount(begin).multiply(new BigDecimal(0.08)).setScale(2,BigDecimal.ROUND_HALF_UP);
                     endAccount = this.monthSubAccount(end).multiply(new BigDecimal(0.1)).setScale(2,BigDecimal.ROUND_HALF_UP);
                    accountB = beginAccount.add(endAccount);

                }else{
                    accountB = account.multiply(new BigDecimal(0.1)).setScale(2,BigDecimal.ROUND_HALF_UP);
                }
                //保存该店家的分佣信息并更新到钱包
                UserAcountHistory history = new UserAcountHistory();
                history.setUserMemberId(upUser.getId());
                history.setNote("来自社群粉丝" + this.getYearMonthStr() + "的月分佣。");
                history.setBalanceHistory(accountB);
                history.setCreateTime(ym);
                history.setBalanceType(0);
                try {
                    userAcountHistoryService.save(history);
                    //更新到用户钱包
                    userBanlanceService.updateBanlance(upUser.getId(), accountB);
                } catch (Exception e) {
                    throw new RRException("分佣异常！"+e);
                }
                //如果同级
                BigDecimal sameLevel = new BigDecimal(0);

                if(upUser.getLevelId()==user.getLevelId()){
                    sameLevel = account.multiply(new BigDecimal(0.03)).setScale(2,BigDecimal.ROUND_HALF_UP);
                    if(userLevelChange.getCreateTime().getTime()>firstDay.getTime()){
                        //升级前为A级，A级店主没有同级分佣，所有取升级之后的
                        sameLevel = endAccount.multiply(new BigDecimal(0.03)).setScale(2,BigDecimal.ROUND_HALF_UP);
                    }
                    UserAcountHistory historySameLevel = new UserAcountHistory();
                    historySameLevel.setUserMemberId(upUser.getId());
                    historySameLevel.setNote("来自社群粉丝" + this.getYearMonthStr() + "的同级孵化分佣奖励。");
                    historySameLevel.setBalanceHistory(sameLevel);
                    historySameLevel.setCreateTime(ym);
                    historySameLevel.setBalanceType(0);
                    try {
                        userAcountHistoryService.save(historySameLevel);
                        //更新到用户钱包
                        userBanlanceService.updateBanlance(upUser.getId(), sameLevel);
                    } catch (Exception e) {
                        throw new RRException("分佣异常！"+e);
                    }
                }

            } else if (upUser.getLevelId() == 4) {//C级店主
                BigDecimal accountC = account.multiply(new BigDecimal(0.1)).setScale(2,BigDecimal.ROUND_HALF_UP);
                BigDecimal endAccount = new BigDecimal(0);
                if(userLevelChange.getCreateTime().getTime()>firstDay.getTime()){

                    List<SubAccountPay> end = subAccountPayService.list(
                            new QueryWrapper<SubAccountPay>().eq("shop_id",user.getId())
                                    .eq("pay_status",2).ge("pay_time",userLevelChange.getCreateTime()).le("pay_time",endTime));

                     endAccount = this.monthSubAccount(end).multiply(new BigDecimal(0.1)).setScale(2,BigDecimal.ROUND_HALF_UP);

                }
                UserAcountHistory history = new UserAcountHistory();
                history.setUserMemberId(upUser.getId());
                history.setNote("来自社群粉丝" + this.getYearMonthStr() + "的月分佣奖励。");
                history.setBalanceHistory(accountC);
                history.setCreateTime(ym);
                history.setBalanceType(0);
                try {
                    userAcountHistoryService.save(history);
                    //更新到用户钱包
                    userBanlanceService.updateBanlance(upUser.getId(), accountC);
                } catch (Exception e) {
                    throw new RRException("分佣异常！"+e);
                }
                //直邀团长分佣
                //看当前的店主是否有下级
                List<UserMember> zyList = this.baseMapper.selectList(new QueryWrapper<UserMember>().eq("parent_id",user.getId()));
                if(zyList.size()>0){
                    BigDecimal zyAccount = account.multiply(new BigDecimal(0.15)).setScale(2,BigDecimal.ROUND_HALF_UP);
                    if(userLevelChange.getCreateTime().getTime()>firstDay.getTime()){
                        zyAccount = endAccount.multiply(new BigDecimal(0.15)).setScale(2,BigDecimal.ROUND_HALF_UP);
                    }
                    UserAcountHistory zyHistory = new UserAcountHistory();
                    zyHistory.setUserMemberId(upUser.getId());
                    zyHistory.setNote("来自社群粉丝" + this.getYearMonthStr() + "的直邀团长分佣奖励。");
                    zyHistory.setBalanceHistory(zyAccount);
                    zyHistory.setCreateTime(ym);
                    zyHistory.setBalanceType(0);
                    try {
                        userAcountHistoryService.save(zyHistory);
                        //更新到用户钱包
                        userBanlanceService.updateBanlance(upUser.getId(), zyAccount);
                    } catch (Exception e) {
                        throw new RRException("分佣异常！"+e);
                    }
                    //间邀团长分佣
                    //查看当前店主的下级是否还有下级
                    BigDecimal jyAccount = new BigDecimal(0);
                      for (UserMember userMember:zyList){
                          List<UserMember> jyList = this.baseMapper.selectList(new QueryWrapper<UserMember>().eq("parent_id",userMember.getId()));
                          if(jyList.size()>0){
                              for (UserMember userMember1:jyList){
                                  List<SubAccountPay> subAccountPays1 = subAccountPayService.list(
                                          new QueryWrapper<SubAccountPay>().eq("shop_id",userMember.getId())
                                                  .eq("pay_status",2).ge("pay_time",startTime)
                                                  .le("pay_time",endTime));
                                  if(userLevelChange.getCreateTime().getTime()>firstDay.getTime()){
                                      subAccountPays1 = subAccountPayService.list(
                                              new QueryWrapper<SubAccountPay>().eq("shop_id",userMember.getId())
                                                      .eq("pay_status",2).ge("pay_time",userLevelChange.getCreateTime())
                                                      .le("pay_time",endTime));
                                  }
                                  BigDecimal count = this.monthSubAccount(subAccountPays1);
                                  jyAccount = count.multiply(new BigDecimal(0.1)).setScale(2,BigDecimal.ROUND_HALF_UP).add(jyAccount);
                              }
                          }

                          UserAcountHistory jyHistory = new UserAcountHistory();
                          jyHistory.setUserMemberId(upUser.getId());
                          jyHistory.setNote("来自社群粉丝" + this.getYearMonthStr() + "的间邀团长分佣奖励。");
                          jyHistory.setBalanceHistory(jyAccount);
                          jyHistory.setCreateTime(ym);
                          jyHistory.setBalanceType(0);
                          try {
                              userAcountHistoryService.save(jyHistory);
                              //更新到用户钱包
                              userBanlanceService.updateBanlance(upUser.getId(), jyAccount);
                          } catch (Exception e) {
                              throw new RRException("分佣异常！"+e);
                          }
                      }
                }

            }
            //查询店主上级的上级
            UserMember upupUser = this.baseMapper.selectOne(new QueryWrapper<UserMember>().eq("id",upUser.getId()));
            if(upupUser != null){
                //判断店主升级时间是否在本月内，如是则以升级时间为节点，计算升级前与升级后的分佣比
                UserLevelChange upupuserLevelChange = userLevelChangeService.getOne(
                        new QueryWrapper<UserLevelChange>().eq("user_member_id",upupUser.getId())
                                .orderByDesc("create_time").last("LIMIT 1"));

                if(upupUser.getLevelId()==2){//A级店主
                    BigDecimal accountA = new BigDecimal(0);
                    if(upupuserLevelChange.getCreateTime().getTime()>firstDay.getTime()){
                        List<SubAccountPay> subAccountPays1 = subAccountPayService.list(
                                new QueryWrapper<SubAccountPay>().eq("shop_id",user.getId())
                                        .eq("pay_status",2).ge("pay_time",upupuserLevelChange.getCreateTime())
                                        .le("pay_time",endTime));
                        account = this.monthSubAccount(subAccountPays1);
                        accountA = account.multiply(new BigDecimal(0.08)).setScale(2,BigDecimal.ROUND_HALF_UP);
                    }else{
                        accountA = account.multiply(new BigDecimal(0.08)).setScale(2,BigDecimal.ROUND_HALF_UP);
                    }

                    UserAcountHistory history = new UserAcountHistory();
                    history.setUserMemberId(upupUser.getId());
                    history.setNote("来自社群粉丝" + this.getYearMonthStr() + "的月分佣。");
                    history.setBalanceHistory(accountA);
                    history.setCreateTime(ym);
                    history.setBalanceType(0);
                    try {
                        userAcountHistoryService.save(history);
                        //更新到用户钱包
                        userBanlanceService.updateBanlance(upupUser.getId(), accountA);
                    } catch (Exception e) {
                        throw new RRException("分佣异常！"+e);
                    }

                }else if (upupUser.getLevelId()==3){

                    BigDecimal accountB = new BigDecimal(0);
                    BigDecimal beginAccount = new BigDecimal(0);
                    BigDecimal  endAccount = new BigDecimal(0);
                    if(upupuserLevelChange.getCreateTime().getTime()>firstDay.getTime()){
                        List<SubAccountPay> begin = subAccountPayService.list(
                                new QueryWrapper<SubAccountPay>().eq("shop_id",user.getId())
                                        .eq("pay_status",2).ge("pay_time",startTime).le("pay_time",upupuserLevelChange.getCreateTime()));
                        List<SubAccountPay> end = subAccountPayService.list(
                                new QueryWrapper<SubAccountPay>().eq("shop_id",user.getId())
                                        .eq("pay_status",2).ge("pay_time",upupuserLevelChange.getCreateTime()).le("pay_time",endTime));
                        beginAccount = this.monthSubAccount(begin).multiply(new BigDecimal(0.08)).setScale(2,BigDecimal.ROUND_HALF_UP);
                        endAccount = this.monthSubAccount(end).multiply(new BigDecimal(0.1)).setScale(2,BigDecimal.ROUND_HALF_UP);
                        accountB = beginAccount.add(endAccount);

                    }else{
                        accountB = account.multiply(new BigDecimal(0.1)).setScale(2,BigDecimal.ROUND_HALF_UP);
                    }
                    //保存该店家的分佣信息并更新到钱包
                    UserAcountHistory history = new UserAcountHistory();
                    history.setUserMemberId(upupUser.getId());
                    history.setNote("来自社群粉丝" + this.getYearMonthStr() + "的月分佣。");
                    history.setBalanceHistory(accountB);
                    history.setCreateTime(ym);
                    history.setBalanceType(0);
                    try {
                        userAcountHistoryService.save(history);
                        //更新到用户钱包
                        userBanlanceService.updateBanlance(upupUser.getId(), accountB);
                    } catch (Exception e) {
                        throw new RRException("分佣异常！"+e);
                    }
                    //如果同级
                    BigDecimal sameLevel = new BigDecimal(0);
                    if(upUser.getLevelId()==user.getLevelId()){
                        sameLevel = account.multiply(new BigDecimal(0.03)).setScale(2,BigDecimal.ROUND_HALF_UP);
                        if(upupuserLevelChange.getCreateTime().getTime()>firstDay.getTime()){
                            sameLevel = endAccount.multiply(new BigDecimal(0.03)).setScale(2,BigDecimal.ROUND_HALF_UP);
                        }
                        UserAcountHistory historySameLevel = new UserAcountHistory();
                        historySameLevel.setUserMemberId(upUser.getId());
                        historySameLevel.setNote("来自社群粉丝" + this.getYearMonthStr() + "的同级孵化分佣奖励。");
                        historySameLevel.setBalanceHistory(sameLevel);
                        historySameLevel.setCreateTime(ym);
                        historySameLevel.setBalanceType(0);
                        try {
                            userAcountHistoryService.save(historySameLevel);
                            //更新到用户钱包
                            userBanlanceService.updateBanlance(upUser.getId(), sameLevel);
                        } catch (Exception e) {
                            throw new RRException("分佣异常！"+e);
                        }
                    }


                }else if (upupUser.getLevelId()==4){

                    BigDecimal accountC = account.multiply(new BigDecimal(0.1)).setScale(2,BigDecimal.ROUND_HALF_UP);
                    BigDecimal endAccount = new BigDecimal(0);
                    if(upupuserLevelChange.getCreateTime().getTime()>firstDay.getTime()){
                        List<SubAccountPay> end = subAccountPayService.list(
                                new QueryWrapper<SubAccountPay>().eq("shop_id",user.getId())
                                        .eq("pay_status",2).ge("pay_time",upupuserLevelChange.getCreateTime()).le("pay_time",endTime));
                        endAccount = this.monthSubAccount(end).multiply(new BigDecimal(0.1)).setScale(2,BigDecimal.ROUND_HALF_UP);
                    }
                    UserAcountHistory history = new UserAcountHistory();
                    history.setUserMemberId(upupUser.getId());
                    history.setNote("来自社群粉丝" + this.getYearMonthStr() + "的月分佣奖励。");
                    history.setBalanceHistory(accountC);
                    history.setCreateTime(ym);
                    history.setBalanceType(0);
                    try {
                        userAcountHistoryService.save(history);
                        //更新到用户钱包
                        userBanlanceService.updateBanlance(upupUser.getId(), accountC);
                    } catch (Exception e) {
                        throw new RRException("分佣异常！"+e);
                    }
                    //如果同级
                    BigDecimal sameLevel = new BigDecimal(0);
                    if(upupUser.getLevelId()==user.getLevelId()){
                        sameLevel = account.multiply(new BigDecimal(0.03)).setScale(2,BigDecimal.ROUND_HALF_UP);
                        if(upupuserLevelChange.getCreateTime().getTime()>firstDay.getTime()){
                            sameLevel = endAccount.multiply(new BigDecimal(0.03)).setScale(2,BigDecimal.ROUND_HALF_UP);
                        }
                        UserAcountHistory historySameLevel = new UserAcountHistory();
                        historySameLevel.setUserMemberId(upupUser.getId());
                        historySameLevel.setNote("来自社群粉丝" + this.getYearMonthStr() + "的同级孵化分佣奖励。");
                        historySameLevel.setBalanceHistory(sameLevel);
                        historySameLevel.setCreateTime(ym);
                        historySameLevel.setBalanceType(0);
                        try {
                            userAcountHistoryService.save(historySameLevel);
                            //更新到用户钱包
                            userBanlanceService.updateBanlance(upupUser.getId(), sameLevel);
                        } catch (Exception e) {
                            throw new RRException("分佣异常！"+e);
                        }
                    }
                    //直邀团长分佣
                    //看当前的店主是否有下级
                    List<UserMember> zyList = this.baseMapper.selectList(new QueryWrapper<UserMember>().eq("parent_id",user.getId()));
                    if(zyList.size()>0){
                            BigDecimal zyAccount = account.multiply(new BigDecimal(0.15)).setScale(2,BigDecimal.ROUND_HALF_UP);
                        if(upupuserLevelChange.getCreateTime().getTime()>firstDay.getTime()){
                            zyAccount = endAccount.multiply(new BigDecimal(0.15)).setScale(2,BigDecimal.ROUND_HALF_UP);
                        }
                        UserAcountHistory zyHistory = new UserAcountHistory();
                        zyHistory.setUserMemberId(upupUser.getId());
                        zyHistory.setNote("来自社群粉丝" + this.getYearMonthStr() + "的直邀团长分佣奖励。");
                        zyHistory.setBalanceHistory(zyAccount);
                        zyHistory.setCreateTime(ym);
                        zyHistory.setBalanceType(0);
                        try {
                            userAcountHistoryService.save(zyHistory);
                            //更新到用户钱包
                            userBanlanceService.updateBanlance(upupUser.getId(), zyAccount);
                        } catch (Exception e) {
                            throw new RRException("分佣异常！"+e);
                        }
                        //间邀团长分佣
                        //查看当前店主的下级是否还有下级
                        BigDecimal jyAccount = new BigDecimal(0);
                        for (UserMember userMember:zyList){
                            List<UserMember> jyList = this.baseMapper.selectList(new QueryWrapper<UserMember>().eq("parent_id",userMember.getId()));
                            if(jyList.size()>0){
                                for (UserMember userMember1:jyList){
                                    List<SubAccountPay> subAccountPays1 = subAccountPayService.list(
                                            new QueryWrapper<SubAccountPay>().eq("shop_id",userMember1.getId())
                                                    .eq("pay_status",2).ge("pay_time",startTime)
                                                    .le("pay_time",endTime));
                                    if(upupuserLevelChange.getCreateTime().getTime()>firstDay.getTime()){
                                        subAccountPays1 = subAccountPayService.list(
                                                new QueryWrapper<SubAccountPay>().eq("shop_id",userMember1.getId())
                                                        .eq("pay_status",2).ge("pay_time",upupuserLevelChange.getCreateTime())
                                                        .le("pay_time",endTime));
                                    }
                                    BigDecimal count = this.monthSubAccount(subAccountPays1);
                                    jyAccount = count.multiply(new BigDecimal(0.1)).setScale(2,BigDecimal.ROUND_HALF_UP).add(jyAccount);
                                }
                            }

                            UserAcountHistory jyHistory = new UserAcountHistory();
                            jyHistory.setUserMemberId(upupUser.getId());
                            jyHistory.setNote("来自社群粉丝" + this.getYearMonthStr() + "的间邀团长分佣奖励。");
                            jyHistory.setBalanceHistory(jyAccount);
                            jyHistory.setCreateTime(ym);
                            jyHistory.setBalanceType(0);
                            try {
                                userAcountHistoryService.save(jyHistory);
                                //更新到用户钱包
                                userBanlanceService.updateBanlance(upupUser.getId(), jyAccount);
                            } catch (Exception e) {
                                throw new RRException("分佣异常！"+e);
                            }
                        }
                    }


                }
            }
        }
    }

    /**
     * 查询不存在下级店家的店家（最少是A级店家，因为最少是A级店家才可以铺货）
     */
    @Override
    public  List<UserMember> noSubUser(){
        return userMemberDao.getNoSub();
    }

    /**
     * 获取本月月份(yyyy-MM)
     */
    public String getYearMonthStr(){
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return (month+"-"+day);
    }

    /**
     * 计算月分账流水
     * @param list
     * @return
     */
    public BigDecimal monthSubAccount(List<SubAccountPay> list){
        BigDecimal total = new BigDecimal(0);
        if(list.size()>0){
            for (SubAccountPay subAccountPay:list){
                total = total.add(subAccountPay.getPayMoney());
            }
        }
        return total;
    }

    /**
     * 获取当前年月日（yyyy-MM）
     * @return
     */
    public Date getYearMonth() throws Exception{
        //获取年月
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date day = new Date();
        Date result = sdf.parse(sdf.format(day));
        return result;
    }

    /**
     * 获取指定日期的Day
     * @param date
     * @return
     */
    public BigDecimal getAppointDay(Date date){
//        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime dateTime = new DateTime(date);
        return new BigDecimal(dateTime.getDayOfMonth());
    }

    /**
     * 获取当月最大天数
     * @return
     */
    public BigDecimal getLastDayForMonth(){
        Calendar cal = Calendar.getInstance();
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return new BigDecimal(maxDay);
    }


    /**
     * 获取本月第一天零时
     * @return
     */
    public  Date getmindate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date a = calendar.getTime();
        return a;
    }

    /**
     *获取当前用户的社群人数
     */
    @Override
    public  List<UserMember> getChildNum(Long id){
        List<UserMember> list = userMemberDao.getChildNum(id);
        return list;
    }


    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    /**
     * 根据字符串生成对应的二维码图片png
     * 大小:200*200
     *
     * content：要转换的内容
     * path：生成的二维码图片的绝对路径
     * filename: 生成的文件名
     */
    public  void buildQuickMark(String content, String path, String filename) throws Exception {
        try {
            BitMatrix byteMatrix = new MultiFormatWriter().encode(new String(content.getBytes(), "iso-8859-1"),
                    BarcodeFormat.QR_CODE, 200, 200);
            String format = "png";
            File file = new File(path+"\\"+filename+"."+format);
            BufferedImage image = toBufferedImage(byteMatrix);
            if (!ImageIO.write(image, format, file)) {
                throw new IOException("Could not write an image of format " + format + " to " + file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    @Override
    public String getCodes(Long id) throws Exception{
        String qrcodeUrl = "";
        //先查询用户表是否存在二维码
        UserMember userMember = this.baseMapper.selectOne(new QueryWrapper<UserMember>().eq("id",id));
        if(userMember.getQrCode()!=null&&!"".equals(userMember.getQrCode())){
            qrcodeUrl = userMember.getQrCode();
        }else {


        //现获取邀请码
//        String content = this.inviteCode(id);
            String content = "808245";
        System.out.println("inviteCode=====:"+content);
//        String yum = "https://kbb.aissyun.com/pages/GYL-index/index?inviteCode="+content;
        String yum = "https://kbb.aissyun.com/weixCode?inviteCode="+content;
        String path = "";

        String domain = "http://rongka-appimage.oss-cn-shanghai.aliyuncs.com/";
        // 使用UUID重命名文件
         String fileName = UUID.randomUUID().toString().replace("-", "")+".png";

        // 上传的URI
//        String uri = String.join("/", "pos", fileName);
            System.out.println("+==========================");
System.out.println(domain+"pos/"+fileName+".png");
System.out.println("+==========================");
        // 本地备份文件夹
        String localFolder = "D:\\upload";
        this.buildQuickMark(yum,localFolder,fileName);
        path = localFolder+"\\"+fileName+".png";

        //读取文件上传到OSS
//        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        String filepath = path;//D盘下的file文件夹的目录
        File file = new File(filepath);//File类型可以是文件也可以是文件夹
            FileInputStream inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
            Object url = uploadService.upload(multipartFile);
            qrcodeUrl = url.toString();
            System.out.println(url);
            //将图片地址保存到用户表
            userMember.setQrCode(url.toString());
            try {
                this.baseMapper.updateById(userMember);
            } catch (Exception e) {
                throw new RRException("二维码获取失败！");
            }
        }


        return qrcodeUrl;
    }

    /**
     * 我的收益
     * @param userId
     * @return
     */
    @Override
    public  Map<String,Object> myEarnings(Long userId){
        Map<String,Object> map = new HashMap<>();
        //本月收益
        StoreRecord storeRecord =   storeRecordService.getMonth(userId);
        BigDecimal month = new BigDecimal(0);
        if(storeRecord!=null) {
            month = storeRecord.getCostFee();
        }
        map.put("month",month);
        //累计收益
        StoreRecord storeRecord1 = storeRecordService.getMonth(userId);
        BigDecimal all = new BigDecimal(0);
        if(storeRecord1!=null) {
            all = storeRecord1.getCostFee();
        }
        map.put("all",all);
        //今日订单
        OrderOrder order = orderOrderService.getOrderNum(userId);
        BigDecimal orderNum = new BigDecimal(0);
        if(order!=null) {
            orderNum = order.getPayAmount();
        }
        map.put("order",orderNum);
        return map;
    }

}
