package com.example.payment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * Entity representing a payment method.
 * Maps to the "payway" table in the database.
 */
@Data
@TableName(value = "payway") // Corresponds to the database table name
public class PayWay {

    /**
     * Unique identifier for the payment method.
     * Automatically generated primary key.
     */
    @TableId(type = IdType.AUTO) // Primary key, auto-increment
    private Integer id;

    /**
     * Name or type of the payment method.
     */
    private String payway;

    /**
     * Email associated with the payment method.
     */
    private String email;

    /**
     * Flag indicating whether this payment method is the default.
     * Typically, 1 for default and 0 for non-default.
     */
    private Integer isDefault;
}