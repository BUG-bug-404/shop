package com.keith.modules.service.address.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keith.common.exception.RRException;
import com.keith.common.utils.Query;
import com.keith.modules.dao.address.UserReceiveAddressDao;
import com.keith.modules.dto.address.AddressDTO;
import com.keith.modules.entity.address.UserReceiveAddress;
import com.keith.modules.service.address.UserReceiveAddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.keith.common.utils.PageUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service("userReceiveAddressService")
public class UserReceiveAddressServiceImpl extends ServiceImpl<UserReceiveAddressDao, UserReceiveAddress> implements UserReceiveAddressService {

    @Override
    public List<UserReceiveAddress>  getList(Long userId){
        return this.baseMapper.selectList(new QueryWrapper<UserReceiveAddress>().eq("user_member_id",userId));
    }
    /**
     * 地址新增
     */

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void add(UserReceiveAddress userReceiveAddress,Long userId){

        try {
            //判断该次修改或新增是否选为默认，如果是则需要先将已存在的默认清除
            if(userReceiveAddress.getDefaultStatus()==1){
                UserReceiveAddress address = this.baseMapper.selectOne(
                        new QueryWrapper<UserReceiveAddress>().eq("user_member_id",userId).eq("default_status",1));
                if(address != null){
                    UserReceiveAddress addressUpdate = new UserReceiveAddress();
                    addressUpdate.setId(address.getId());
                    addressUpdate.setDefaultStatus(0);
                    this.baseMapper.updateById(addressUpdate);
                }
            }

            if(userReceiveAddress.getId()==null||"".equals(userReceiveAddress.getId())){
                userReceiveAddress.setUserMemberId(userId);
                userReceiveAddress.setCreateTime(new Date());
                this.baseMapper.insert(userReceiveAddress);
            }else{
                this.baseMapper.updateById(userReceiveAddress);
            }
        } catch (Exception e) {
            throw new RRException("保存失败",e);
        }

    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateStatus(Long id,Long userId){
        //根据用户ID查出该用户地址列表，如已有默认则修改为不默认
        UserReceiveAddress userReceiveAddress = this.baseMapper.selectOne(
                new QueryWrapper<UserReceiveAddress>().eq("user_member_id",userId).eq("default_status",1));
        try {
            if(userReceiveAddress != null){
                UserReceiveAddress address = new UserReceiveAddress();
                address.setId(userReceiveAddress.getId());
                address.setDefaultStatus(0);
                this.baseMapper.updateById(address);
            }
            UserReceiveAddress updateAddress = new UserReceiveAddress();
            updateAddress.setId(id);
            updateAddress.setDefaultStatus(1);
            this.baseMapper.updateById(updateAddress);
        } catch (Exception e) {
            throw new RRException("修改默认失败！",e);
        }

    }
}
