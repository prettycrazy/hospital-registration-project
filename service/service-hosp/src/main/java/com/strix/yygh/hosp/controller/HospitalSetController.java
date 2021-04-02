package com.strix.yygh.hosp.controller;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.strix.yygh.common.result.Result;
import com.strix.yygh.common.utils.MD5;
import com.strix.yygh.model.hosp.Hospital;
import com.strix.yygh.model.hosp.HospitalSet;
import com.strix.yygh.hosp.service.HospitalSetService;
import com.strix.yygh.vo.hosp.HospitalSetQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

//   1查询医院设置表中的所有信息
    @GetMapping("findAll")
    public Result findAllHospitalSet(){
//        调用service里面的方法
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

//  2逻辑删除医院设置
    @DeleteMapping("{id}")
    public Result removeHospitalSet(@PathVariable Long id){
        boolean flag = hospitalSetService.removeById(id);
        if(flag){
            return Result.ok();
        }
        else{
            return Result.fail();
        }
    }


//    3条件查询带分页
    @PostMapping("findPage/{current}/{limit}")
    public Result findPageHospSet(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){
//        创建page对象
        Page<HospitalSet> page = new Page<>(current,limit);
//          构建条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();

        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();

        if(!StringUtils.isEmpty(hosname)){
            wrapper.like("hosname",hospitalSetQueryVo.getHosname());
        }

        if(!StringUtils.isEmpty(hoscode)){
            wrapper.like("hoscode",hospitalSetQueryVo.getHosname());
        }
//        调用方法实现分页查询
        Page<HospitalSet> pageHospitalSet = hospitalSetService.page(page, wrapper);

        return Result.ok(pageHospitalSet);
    }


//    4添加医院设置
    @PostMapping
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet){
//        设定状态为1
        hospitalSet.setStatus(1);
//      签名密钥设计
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));
//        调用service
        boolean save = hospitalSetService.save(hospitalSet);
        if(save){
            return Result.ok();
        } else {
            return Result.fail();
        }

    }


//    5根据id获取医院位置
    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

//    6修改医院设置
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet){
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if(flag){
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

//    7批量删除医院设置
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList){
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }

//    8医院设置锁定和解锁
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

//    9发送签名密钥
    @PutMapping("sendKey/{id}")
    public Result sendHospitalKey(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
//        TODO 发送短信
        return Result.ok();
    }


}
