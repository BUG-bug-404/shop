package com.keith.modules.service.address;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.keith.common.utils.PageUtils;
import com.keith.modules.dto.address.AddressDTO;
import com.keith.modules.entity.address.UserReceiveAddress;

import java.util.List;
import java.util.Map;

/**
 * 收货地址表
 *
 * @author lijinxinag
 * @email @qq.com
 * @date 2020-06-02 20:08:46
 */
public interface UserReceiveAddressService extends IService<UserReceiveAddress> {

    List<UserReceiveAddress>  getList(Long userId);

    /**
     * 地址新增
     */
    void add(UserReceiveAddress userReceiveAddress,Long userId);
    /**
     * 修改默认地址
     */
    void updateStatus(Long id,Long userId);

}

