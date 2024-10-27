package com.example.payment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "payway")
public class PayWay {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String payway;
    private String email;
    private Integer isDefault;


}
