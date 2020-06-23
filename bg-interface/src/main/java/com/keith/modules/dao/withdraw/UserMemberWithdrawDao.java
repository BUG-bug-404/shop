package com.keith.modules.dao.withdraw;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keith.modules.entity.withdraw.UserMemberWithdraw;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 提现申请表
 * 
 * @author lijinxiang
 * @email @qq.com
 * @date 2020-06-13 13:47:48
 */
@Mapper
@Repository
public interface UserMemberWithdrawDao extends BaseMapper<UserMemberWithdraw> {
    @Select("SELECT COUNT(id) FROM user_member_withdraw WHERE DATE_FORMAT( create_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' ) AND user_member_id =#{userId}")
    int countNum(@Param("userId") long userId);
	
}
