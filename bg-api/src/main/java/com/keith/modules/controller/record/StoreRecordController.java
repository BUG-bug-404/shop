package com.keith.modules.controller.record;

import com.keith.common.utils.PageUtils;
import com.keith.common.utils.R;
import com.keith.common.validator.ValidatorUtils;
import com.keith.modules.entity.record.StoreRecord;
import com.keith.modules.service.record.StoreRecordService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 分销记录表
 *
 * @author lius
 * @email @qq.com
 * @date 2020-06-17 15:05:30
 */
@Api(tags = "分销记录表")
@RestController
@RequestMapping("/storeRecord")
public class StoreRecordController {
    @Autowired
    private StoreRecordService storeRecordService;

    /**
     * 列表
     */
    @PostMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = storeRecordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @PostMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        StoreRecord storeRecord = storeRecordService.getById(id);

        return R.ok().put("storeRecord", storeRecord);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody StoreRecord storeRecord){
        storeRecordService.save(storeRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody StoreRecord storeRecord){
        ValidatorUtils.validateEntity(storeRecord);
        storeRecordService.updateById(storeRecord);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        storeRecordService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
