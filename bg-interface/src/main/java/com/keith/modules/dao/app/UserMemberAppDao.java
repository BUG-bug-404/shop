package com.keith.modules.dao.app;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.app.UserMemberApp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author gray
 * @version 1.0
 * @date 2020/6/13 15:40
 */
@Repository
@Mapper
public interface UserMemberAppDao extends BaseMapper<UserMemberApp> {
}
