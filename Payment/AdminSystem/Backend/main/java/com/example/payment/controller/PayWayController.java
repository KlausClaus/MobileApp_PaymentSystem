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

    /**
     * Service for payment method-related operations.
     */
    @Resource
    private IPayWayService payWayService;

    /**
     * Add or update a payment method.
     * If the payment method already exists, it will be updated; otherwise, a new payment method will be added.
     *
     * @param payWay The payment method object to be added or updated.
     * @return A {@code Result} object containing the last added or updated payment method.
     */
    @PostMapping
    public Result save(@RequestBody PayWay payWay) {
        payWayService.saveOrUpdate(payWay);
        return Result.sucess(payWayService.list().get(payWayService.list().size()-1));
    }

    /**
     * Query all payment methods with optional name filtering.
     *
     * @param name The name filter (optional, default is an empty string).
     * @return A {@code Result} object containing the list of payment methods.
     */
    @GetMapping("/list")
    public Result findAll(@RequestParam(defaultValue = "") String name) {
        QueryWrapper<PayWay> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        return Result.sucess(payWayService.list(queryWrapper));
    }

    /**
     * Query payment methods by email.
     *
     * @param payWay The payment method object containing the email.
     * @return A {@code Result} object containing the list of payment methods for the given email.
     */
    @PostMapping("/listByEmail")
    public Result listByEmail(@RequestBody PayWay payWay) {
        QueryWrapper<PayWay> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", payWay.getEmail());
        return Result.sucess(payWayService.list(queryWrapper));
    }

    /**
     * Paginate through payment methods with optional name and user ID filtering.
     *
     * @param pageNum  The page number to retrieve.
     * @param pageSize The number of records per page.
     * @param uid      The user ID filter (use -1 to ignore).
     * @param name     The name filter (optional, default is an empty string).
     * @return A {@code Result} object containing the paginated list of payment methods.
     */
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

    /**
     * Delete a payment method by its ID.
     *
     * @param id The ID of the payment method to be deleted.
     * @return A {@code Result} object indicating whether the deletion was successful.
     */
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.sucess(payWayService.removeById(id)) ;
    }

    /**
     * Batch delete payment methods by their IDs.
     *
     * @param ids A list of payment method IDs to be deleted.
     * @return A {@code Result} object indicating whether the deletion was successful.
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.sucess(payWayService.removeByIds(ids));
    }


}
