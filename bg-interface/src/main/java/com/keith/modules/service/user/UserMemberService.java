package com.keith.modules.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.modules.dto.SignInDTO;
import com.keith.modules.entity.user.UserMember;

/**
 * @author gray
 * @version 1.0
 * @date 2020/6/12 16:19
 */
public interface UserMemberService extends IService<UserMember> {



    UserMember userMemberLogin(SignInDTO signInDTO);
}
