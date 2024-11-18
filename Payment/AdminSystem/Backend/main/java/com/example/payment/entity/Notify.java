package com.example.payment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Entity representing a notification.
 * Maps to the "notify" table in the database.
 */
@Data
@TableName(value = "notify")
public class Notify {
    /**
     * Unique identifier for the notification.
     * Automatically generated primary key.
     */
    @TableId(type = IdType.AUTO) // Primary key, auto-increment
    private Integer id;

    /**
     * User ID associated with the notification.
     */
    private Integer uid;

    /**
     * Content of the notification.
     */
    private String content;

    /**
     * Time when the notification was created.
     */
    private String time;

}
