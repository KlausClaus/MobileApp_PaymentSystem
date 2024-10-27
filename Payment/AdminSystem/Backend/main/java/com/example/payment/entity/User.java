package com.example.payment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "user")
public class User{
    @TableId(type = IdType.AUTO)
    private Integer uid;
    private String username;
    private String password;
    private Integer notify;
    private String avatar;

    private String role;

}
