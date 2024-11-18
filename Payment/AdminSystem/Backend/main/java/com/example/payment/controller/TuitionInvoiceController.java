package com.example.payment.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.payment.common.Result;
import com.example.payment.entity.TuitionInvoice;
import com.example.payment.entity.User;
import com.example.payment.service.ITuitionInvoiceService;
import com.example.payment.service.IUserService;
import com.example.payment.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/tuitionInvoice")
public class TuitionInvoiceController {

    /**
     * Service for tuition invoice-related operations.
     */
    @Resource
    private ITuitionInvoiceService tuitionInvoiceService;
    /**
     * Service for user-related operations.
     */
    @Resource
    private IUserService userService;

    /**
     * Add or update a tuition invoice record.
     * If the record exists, it will be updated; otherwise, a new record will be added.
     *
     * @param tuitionInvoice The tuition invoice object to be added or updated.
     * @return A {@code Result} object containing the user associated with the given invoice.
     */
    @PostMapping
    public Result save(@RequestBody TuitionInvoice tuitionInvoice) {
        tuitionInvoiceService.saveOrUpdate(tuitionInvoice);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", tuitionInvoice.getStudentEmail());
        User user = userService.getOne(queryWrapper);

        return Result.sucess(user);
    }

    /**
     * Query all tuition invoice records with an optional name filter.
     *
     * @param name The name filter (optional, default is an empty string).
     * @return A {@code Result} object containing the list of tuition invoices.
     */
    @GetMapping("/list")
    public Result findAll(@RequestParam(defaultValue = "") String name) {
        QueryWrapper<TuitionInvoice> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }

        return Result.sucess(tuitionInvoiceService.list(queryWrapper));
    }

    /**
     * Query tuition invoices by student email.
     *
     * @param tuitionInvoice The tuition invoice object containing the student's email.
     * @return A {@code Result} object containing the list of tuition invoices for the given email.
     */
    @PostMapping("/listByEmail")
    public Result listByEmail(@RequestBody TuitionInvoice tuitionInvoice) {
        System.out.println(tuitionInvoice);
        QueryWrapper<TuitionInvoice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_email", tuitionInvoice.getStudentEmail());
        queryWrapper.eq("status",0);
        return Result.sucess(tuitionInvoiceService.list(queryWrapper));
    }

    /**
     * Query tuition invoices by student email and status.
     *
     * @param tuitionInvoice The tuition invoice object containing the student's email and status.
     * @return A {@code Result} object containing the list of tuition invoices matching the criteria.
     */
    @PostMapping("/listByEmailStatus")
    public Result listByEmailStatus(@RequestBody TuitionInvoice tuitionInvoice) {
        System.out.println(tuitionInvoice);
        QueryWrapper<TuitionInvoice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_email", tuitionInvoice.getStudentEmail());
        queryWrapper.eq("status",tuitionInvoice.getStatus());
        return Result.sucess(tuitionInvoiceService.list(queryWrapper));
    }

    /**
     * Paginate through tuition invoices with an optional student name filter.
     *
     * @param pageNum  The page number to retrieve.
     * @param pageSize The number of records per page.
     * @param name     The student name filter (optional, default is an empty string).
     * @return A {@code Result} object containing the paginated list of tuition invoices.
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {
        IPage<TuitionInvoice> page = new Page<>(pageNum, pageSize);
        QueryWrapper<TuitionInvoice> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("student_name", name);
        }


        queryWrapper.orderByDesc("id");
        return Result.sucess(tuitionInvoiceService.page(page, queryWrapper));
    }

    /**
     * Delete a tuition invoice by its ID.
     *
     * @param id The ID of the tuition invoice to be deleted.
     * @return A {@code Result} object indicating whether the deletion was successful.
     */
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.sucess(tuitionInvoiceService.removeById(id)) ;
    }

    /**
     * Batch delete tuition invoices by their IDs.
     *
     * @param ids A list of IDs of the tuition invoices to be deleted.
     * @return A {@code Result} object indicating whether the deletion was successful.
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.sucess(tuitionInvoiceService.removeByIds(ids));
    }


}
