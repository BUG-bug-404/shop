package com.keith.modules.service.wx;

import java.io.IOException;
import java.util.Map;

public interface WeXinService {


    Map<String,Object> wxLogin(String code) throws IOException;

    boolean saveUserInfo(WxUserInfo wxUserInfo,Long usermemberId);

}
