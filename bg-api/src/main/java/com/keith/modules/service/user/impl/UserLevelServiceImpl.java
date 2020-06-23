package com.keith.modules.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.modules.dao.user.UserLevelDao;
import com.keith.modules.entity.user.UserLevel;
import com.keith.modules.entity.user.UserMember;
import com.keith.modules.service.user.UserLevelService;
import com.keith.modules.service.user.UserMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.keith.common.utils.PageUtils;
import com.keith.common.utils.Query;



@Service("userLevelService")
public class UserLevelServiceImpl extends ServiceImpl<UserLevelDao, UserLevel> implements UserLevelService {

    @Autowired
    private UserMemberService userMemberService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserLevel> page = this.page(
                new Query<UserLevel>().getPage(params),
                new QueryWrapper<UserLevel>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取所有用户等级权益
     */
    @Override
    public  List<UserLevel> getList(){
        return this.baseMapper.selectList(new QueryWrapper<UserLevel>());
    }
    /**
     * 根据用户ID获取用户对应的等级权益
     */
    @Override
    public UserLevel getByUserId(Long userId){
        //先查询用户信息，根据用户信息拿到该用户的level_id
        UserMember userMember = userMemberService.getOne(new QueryWrapper<UserMember>().eq("id",userId));
        return this.getOne(new QueryWrapper<UserLevel>().eq("id",userMember.getLevelId()));
    }
}
