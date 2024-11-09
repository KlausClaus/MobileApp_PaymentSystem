package com.example.payment.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.payment.common.Result;
import com.example.payment.entity.Student;
import com.example.payment.entity.TuitionInvoice;
import com.example.payment.entity.User;
import com.example.payment.service.IStudentService;
import com.example.payment.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;
    @Resource
    private IStudentService studentService;
    @PostMapping
    public Result save(@RequestBody User user) {
        return Result.sucess(userService.saveOrUpdate(user));
    }
    // 注册
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",user.getUsername());
        if (studentService.list(queryWrapper).size()!=0){
            return Result.sucess(userService.saveOrUpdate(user));
        }else {
            return Result.error("600","please enter the correct email");
        }

    }

    //更新
    @PostMapping("/update")
    public Result update(@RequestBody User user) {
        return Result.sucess(userService.updateById(user));

    }

    @GetMapping("/list")
    public Result find() {
        return Result.sucess(userService.list());

    }
    // 登录
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        queryWrapper.eq("password", user.getPassword());
        queryWrapper.eq("role", user.getRole());

        return Result.sucess(userService.list(queryWrapper));
    }

    @PostMapping("/getOne")
    public Result getOne(@RequestBody User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        return Result.sucess(userService.list(queryWrapper));
    }

    //删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.sucess(userService.removeById(id)) ;
    }


    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username) {
        System.out.println(username);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return Result.sucess(userService.getOne(queryWrapper));
    }



    @GetMapping
    public Result findAll() {
        return Result.sucess(userService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.sucess(userService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username) {
        IPage<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!"".equals(username)) {
            queryWrapper.like("username", username);
        }
        queryWrapper.eq("role", 2);

        queryWrapper.orderByDesc("uid");
        return Result.sucess(userService.page(page, queryWrapper));
    }




}
