package com.example.payment.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.payment.common.Result;
import com.example.payment.entity.Student;
import com.example.payment.service.IStudentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Controller for managing student records.
 * Provides endpoints for adding, updating, querying, and deleting student data.
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    /**
     * Service for student-related operations.
     */
    @Resource
    private IStudentService studentService;

    /**
     * Add or update a student record.
     * If the student already exists, the record will be updated; otherwise, a new record will be created.
     *
     * @param student The student object to be added or updated.
     * @return A {@code Result} object indicating the success of the operation.
     */
    @PostMapping
    public Result save(@RequestBody Student student) {
        return Result.sucess(studentService.saveOrUpdate(student));
    }

    /**
     * Query all student records with an optional name filter.
     *
     * @param name The name filter (optional, default is an empty string).
     * @return A {@code Result} object containing the list of student records.
     */
    @GetMapping("/list")
    public Result findAll(@RequestParam(defaultValue = "") String name) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        return Result.sucess(studentService.list(queryWrapper));
    }


    /**
     * Paginate through student records with an optional name filter.
     *
     * @param pageNum  The page number to retrieve.
     * @param pageSize The number of records per page.
     * @param name     The name filter (optional, default is an empty string).
     * @return A {@code Result} object containing the paginated list of student records.
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {
        IPage<Student> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }

        queryWrapper.orderByDesc("id");
        return Result.sucess(studentService.page(page, queryWrapper));
    }

    /**
     * Delete a student record by its ID.
     *
     * @param id The ID of the student record to be deleted.
     * @return A {@code Result} object indicating whether the deletion was successful.
     */
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.sucess(studentService.removeById(id)) ;
    }

    /**
     * Batch delete student records by their IDs.
     *
     * @param ids A list of IDs of the student records to be deleted.
     * @return A {@code Result} object indicating whether the deletion was successful.
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.sucess(studentService.removeByIds(ids));
    }


}
