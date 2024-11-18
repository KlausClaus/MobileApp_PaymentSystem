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
    /**
     * Service for user-related operations.
     */
    @Resource
    private IUserService userService;
    /**
     * Service for student-related operations.
     */
    @Resource
    private IStudentService studentService;
    /**
     * Add or update a user record.
     *
     * @param user The user object to be added or updated.
     * @return A {@code Result} object indicating the success of the operation.
     */
    @PostMapping
    public Result save(@RequestBody User user) {
        return Result.sucess(userService.saveOrUpdate(user));
    }
    /**
     * Register a new user.
     * Verifies if the provided email exists in the student records before allowing registration.
     *
     * @param user The user object to be registered.
     * @return A {@code Result} object indicating the success or failure of the operation.
     */
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

    /**
     * Update an existing user record.
     *
     * @param user The user object to be updated.
     * @return A {@code Result} object indicating the success of the operation.
     */
    @PostMapping("/update")
    public Result update(@RequestBody User user) {
        return Result.sucess(userService.updateById(user));

    }

    /**
     * Retrieve all user records.
     *
     * @return A {@code Result} object containing the list of all users.
     */
    @GetMapping("/list")
    public Result find() {
        return Result.sucess(userService.list());

    }

    /**
     * User login.
     * Validates the username, password, and role of the user.
     *
     * @param user The user object containing login credentials.
     * @return A {@code Result} object containing the list of matching users.
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        queryWrapper.eq("password", user.getPassword());
        queryWrapper.eq("role", user.getRole());

        return Result.sucess(userService.list(queryWrapper));
    }

    /**
     * Retrieve user records by username.
     *
     * @param user The user object containing the username.
     * @return A {@code Result} object containing the list of matching users.
     */
    @PostMapping("/getOne")
    public Result getOne(@RequestBody User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        return Result.sucess(userService.list(queryWrapper));
    }

    /**
     * Delete a user by ID.
     *
     * @param id The ID of the user to be deleted.
     * @return A {@code Result} object indicating whether the deletion was successful.
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.sucess(userService.removeById(id)) ;
    }

    /**
     * Retrieve a user by username.
     *
     * @param username The username of the user to be retrieved.
     * @return A {@code Result} object containing the user information.
     */
    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username) {
        System.out.println(username);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return Result.sucess(userService.getOne(queryWrapper));
    }


    /**
     * Retrieve all users.
     *
     * @return A {@code Result} object containing the list of all users.
     */
    @GetMapping
    public Result findAll() {
        return Result.sucess(userService.list());
    }

    /**
     * Retrieve a user by ID.
     *
     * @param id The ID of the user to be retrieved.
     * @return A {@code Result} object containing the user information.
     */
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.sucess(userService.getById(id));
    }

    /**
     * Paginate through user records with an optional username filter.
     *
     * @param pageNum  The page number to retrieve.
     * @param pageSize The number of records per page.
     * @param username The username filter (optional, default is an empty string).
     * @return A {@code Result} object containing the paginated list of users.
     */
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
