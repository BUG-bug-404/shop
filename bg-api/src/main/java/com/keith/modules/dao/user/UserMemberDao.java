package com.keith.modules.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.user.UserMember;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户表
 * 
 * @author JohnSon
 * @email 380847163@qq.com
 * @date 2020-05-13 18:15:37
 */
@Mapper
@Repository
public interface UserMemberDao extends BaseMapper<UserMember> {
    /**
     * 查询不存在下级店家的店家
     */
    List<UserMember> getNoSub();
    /**
     * 获取当前用户的社群人数
     */
    List<UserMember> getChildNum(Long id);
}
