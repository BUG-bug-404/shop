package com.keith.modules.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.google.zxing.common.BitMatrix;
import com.keith.common.utils.PageUtils;
import com.keith.modules.dto.PageDTO;
import com.keith.modules.dto.user.AddUserDTO;
import com.keith.modules.dto.user.UpdateUserDTO;
import com.keith.modules.dto.user.UserForgetDTO;
import com.keith.modules.entity.user.UserMember;
import org.springframework.web.bind.annotation.RequestAttribute;
import springfox.documentation.annotations.ApiIgnore;


import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 用户表
 *
 * @author JohnSon
 * @email 380847163@qq.com
 * @date 2020-05-13 18:15:37
 */
public interface UserMemberService extends IService<UserMember> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 分页获取
     * @return
     */
    PageUtils getPage(PageDTO pageDTO);
    List<UserMember> All();
    /**
     * 用户登录获取用户信息
     * @param  userId
     * @return
     */
    UserMember login(Long userId);

    /**
     * 修改支付密码
     * @param updateUserDTO
     */
    void updatePassword(UpdateUserDTO updateUserDTO,Long userId);

    /**
     * 保存新用户信息
     * @param addUserDTO
     */
    void insertWx(AddUserDTO addUserDTO,Long userId) throws IOException;

    /**
     * 用户昵称更新
     * @param updateUserDTO
     */
    void updateNc(UpdateUserDTO updateUserDTO,Long userId);

    /**
     * 支付密码设置
     */
    void setPayCode(UpdateUserDTO updateUserDTO,Long userId);
    /**
     * 支付密码验证
     */
    Boolean validatePayCode(UpdateUserDTO updateUserDTO,Long userId);
    /**
     * 通过Id获取用户信息
     */
     UserMember getInfo(Long userd);
    /**
     * 修改登录密码
     */
    void updateLoginPassword(UpdateUserDTO updateUserDTO,Long userId);

    /**
     * 忘记登录密码
     */
    void forgetLoginPassword(UserForgetDTO userForgetDTO,Long userId);
    /**
     * 忘记支付密码
     */
    void forgetPayPassword(UserForgetDTO userForgetDTO,Long userId);
    /**
     * 判断是否有支付密码
     */
    Boolean validateExsit(Long userId);

    /**
     * 邀请码生成
     * @return
     */
    String inviteCode(Long userId);

    /**
     * 更新用户邀请的A级店长人数同时判断是否达到自身等级提示条件
     */
     void updateCount(Long id);

    /**
     * 获取该用户的上级用户
     */
    UserMember getParentInfo(Long id);

    void forNoP() throws Exception;

    /**
     * 查询不存在下级店家的店家
     */
    List<UserMember> noSubUser();

    /**
     *获取当前用户的社群人数
     */
    List<UserMember> getChildNum(Long id);

    /**
     * 生产成二维码
     * @return
     */
    String getCodes(Long id) throws Exception;

    /**
     * 我的收益
     * @param userId
     * @return
     */
    Map<String,Object> myEarnings(Long userId);
}

