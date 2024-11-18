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

    /**
     * Service for notification-related operations.
     */
    @Resource
    private INotifyService notifyService;


    /**
     * Add or update a notification.
     * If the notification already exists, it will be updated; otherwise, a new notification will be added.
     *
     * @param notify The notification object to be added or updated.
     * @return A {@code Result} object containing the operation result.
     */
    @PostMapping
    public Result save(@RequestBody Notify notify) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = now.format(formatter);
        notify.setTime(formattedDateTime);
        return Result.sucess(notifyService.saveOrUpdate(notify));
    }

    /**
     * Query notifications by user ID.
     *
     * @param notify The notification object containing the user ID.
     * @return A {@code Result} object containing the list of notifications for the given user ID.
     */
    @PostMapping("/listByUid")
    public Result findAll(@RequestBody Notify notify) {
        QueryWrapper<Notify> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",notify.getUid());
        return Result.sucess(notifyService.list(queryWrapper));
    }


    /**
     * Paginate through notifications with optional name filtering.
     *
     * @param pageNum  The page number to retrieve.
     * @param pageSize The number of records per page.
     * @param name     The name filter (optional).
     * @return A {@code Result} object containing the paginated list of notifications.
     */
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

    /**
     * Delete a notification by its ID.
     *
     * @param id The ID of the notification to be deleted.
     * @return A {@code Result} object indicating whether the deletion was successful.
     */
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.sucess(notifyService.removeById(id)) ;
    }

    /**
     * Batch delete notifications by their IDs.
     *
     * @param ids A list of notification IDs to be deleted.
     * @return A {@code Result} object indicating whether the deletion was successful.
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.sucess(notifyService.removeByIds(ids));
    }


}
