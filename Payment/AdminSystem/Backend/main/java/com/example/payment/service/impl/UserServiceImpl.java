package com.example.payment.service.impl;

import cn.hutool.log.Log;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.payment.entity.User;
import com.example.payment.mapper.UserMapper;

import com.example.payment.service.IUserService;

import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log LOG = Log.get();



}
