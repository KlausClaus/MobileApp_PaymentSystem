package com.example.payment.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.payment.common.Result;
import com.example.payment.entity.Notify;
import com.example.payment.entity.Student;
import com.example.payment.service.IStudentService;
import com.example.payment.service.INotifyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/notify")
public class NotifyController {

    @Resource
    private INotifyService notifyService;


    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Notify notify) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = now.format(formatter);
        notify.setTime(formattedDateTime);
        return Result.sucess(notifyService.saveOrUpdate(notify));
    }

    //查询
    @PostMapping("/listByUid")
    public Result findAll(@RequestBody Notify notify) {
        QueryWrapper<Notify> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",notify.getUid());
        return Result.sucess(notifyService.list(queryWrapper));
    }



    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {
        IPage<Notify> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Notify> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }

        queryWrapper.orderByDesc("id");
        return Result.sucess(notifyService.page(page, queryWrapper));
    }

    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.sucess(notifyService.removeById(id)) ;
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.sucess(notifyService.removeByIds(ids));
    }


}
