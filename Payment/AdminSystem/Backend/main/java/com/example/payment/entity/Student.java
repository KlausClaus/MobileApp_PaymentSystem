package com.example.payment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "student")
public class Student {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String email;
    private String grade;
    private String classes;
    private String gender;
    private String professional;


}
