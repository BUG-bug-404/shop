package com.keith.modules.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.user.UserMember;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author gray
 * @version 1.0
 * @date 2020/6/12 16:25
 */
@Mapper
@Repository
public interface UserMemberDao  extends BaseMapper<UserMember> {
}
