package com.example.payment.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.payment.common.Result;
import com.example.payment.entity.TuitionInvoice;
import com.example.payment.service.ITuitionInvoiceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/tuitionInvoice")
public class TuitionInvoiceController {

    @Resource
    private ITuitionInvoiceService tuitionInvoiceService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody TuitionInvoice tuitionInvoice) {
        return Result.sucess(tuitionInvoiceService.saveOrUpdate(tuitionInvoice));
    }

    //查询
    @GetMapping("/list")
    public Result findAll(@RequestParam(defaultValue = "") String name) {
        QueryWrapper<TuitionInvoice> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }

        return Result.sucess(tuitionInvoiceService.list(queryWrapper));
    }

    @PostMapping("/listByEmail")
    public Result listByEmail(@RequestBody TuitionInvoice tuitionInvoice) {
        System.out.println(tuitionInvoice);
        QueryWrapper<TuitionInvoice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_email", tuitionInvoice.getStudentEmail());
        queryWrapper.eq("status",0);
        return Result.sucess(tuitionInvoiceService.list(queryWrapper));
    }

    @PostMapping("/listByEmailStatus")
    public Result listByEmailStatus(@RequestBody TuitionInvoice tuitionInvoice) {
        System.out.println(tuitionInvoice);
        QueryWrapper<TuitionInvoice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_email", tuitionInvoice.getStudentEmail());
        queryWrapper.eq("status",tuitionInvoice.getStatus());
        return Result.sucess(tuitionInvoiceService.list(queryWrapper));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {
        IPage<TuitionInvoice> page = new Page<>(pageNum, pageSize);
        QueryWrapper<TuitionInvoice> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }


        queryWrapper.orderByDesc("id");
        return Result.sucess(tuitionInvoiceService.page(page, queryWrapper));
    }

    //单个删除
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.sucess(tuitionInvoiceService.removeById(id)) ;
    }

    //批量删除
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.sucess(tuitionInvoiceService.removeByIds(ids));
    }


}
