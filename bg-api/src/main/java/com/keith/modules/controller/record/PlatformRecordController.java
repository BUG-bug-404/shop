package com.keith.modules.controller.record;

import com.keith.common.utils.PageUtils;
import com.keith.common.utils.R;
import com.keith.common.validator.ValidatorUtils;
import com.keith.modules.entity.record.PlatformRecord;
import com.keith.modules.service.record.PlatformRecordService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 平台收入记录表
 *
 * @author lius
 * @email @qq.com
 * @date 2020-06-17 15:05:31
 */
@Api(tags = "平台收入记录表")
@RestController
@RequestMapping("/platformRecord")
public class PlatformRecordController {
    @Autowired
    private PlatformRecordService platformRecordService;

    /**
     * 列表
     */
    @PostMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = platformRecordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @PostMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        PlatformRecord platformRecord = platformRecordService.getById(id);

        return R.ok().put("platformRecord", platformRecord);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody PlatformRecord platformRecord){
        platformRecordService.save(platformRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody PlatformRecord platformRecord){
        ValidatorUtils.validateEntity(platformRecord);
        platformRecordService.updateById(platformRecord);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        platformRecordService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
