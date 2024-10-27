package com.example.payment.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.payment.common.Result;
import com.example.payment.entity.PayWay;
import com.example.payment.service.IPayWayService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/payway")
public class PayWayController {

    @Resource
    private IPayWayService payWayService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody PayWay payWay) {
        payWayService.saveOrUpdate(payWay);
        return Result.sucess(payWayService.list().get(payWayService.list().size()-1));
    }

    //查询
    @GetMapping("/list")
    public Result findAll(@RequestParam(defaultValue = "") String name) {
        QueryWrapper<PayWay> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        return Result.sucess(payWayService.list(queryWrapper));
    }

    @PostMapping("/listByEmail")
    public Result listByEmail(@RequestBody PayWay payWay) {
        QueryWrapper<PayWay> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", payWay.getEmail());
        return Result.sucess(payWayService.list(queryWrapper));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam Integer uid,
                           @RequestParam(defaultValue = "") String name) {
        IPage<PayWay> page = new Page<>(pageNum, pageSize);
        QueryWrapper<PayWay> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        if (uid!=-1){
            queryWrapper.eq("uid", uid);
        }

        queryWrapper.orderByDesc("id");
        return Result.sucess(payWayService.page(page, queryWrapper));
    }

    //单个删除
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.sucess(payWayService.removeById(id)) ;
    }

    //批量删除
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.sucess(payWayService.removeByIds(ids));
    }


}
