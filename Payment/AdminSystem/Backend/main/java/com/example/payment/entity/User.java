package com.example.payment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Entity representing a user.
 * Maps to the "user" table in the database.
 */
@Data
@TableName(value = "user") // Corresponds to the database table name
public class User {

    /**
     * Unique identifier for the user.
     * Automatically generated primary key.
     */
    @TableId(type = IdType.AUTO) // Primary key, auto-increment
    private Integer uid;

    /**
     * Username of the user.
     */
    private String username;

    /**
     * Password of the user.
     */
    private String password;

    /**
     * Notification status for the user.
     * Represents whether the user has notifications enabled or not.
     */
    private Integer notify;

    /**
     * Avatar URL or image path for the user's profile picture.
     */
    private String avatar;

    /**
     * Role of the user (e.g., admin, student, etc.).
     */
    private String role;
}
