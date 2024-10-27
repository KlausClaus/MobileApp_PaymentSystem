package com.example.payment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "notify")
public class Notify {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer uid;
    private String content;
    private String time;

}
