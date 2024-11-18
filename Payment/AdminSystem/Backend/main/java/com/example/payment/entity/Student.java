package com.example.payment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * Entity representing a student.
 * Maps to the "student" table in the database.
 */
@Data
@TableName(value = "student") // Corresponds to the database table name
public class Student {

    /**
     * Unique identifier for the student.
     * Automatically generated primary key.
     */
    @TableId(type = IdType.AUTO) // Primary key, auto-increment
    private Integer id;

    /**
     * Name of the student.
     */
    private String name;

    /**
     * Email address of the student.
     */
    private String email;

    /**
     * Grade of the student.
     */
    private String grade;

    /**
     * Class the student belongs to.
     */
    private String classes;

    /**
     * Gender of the student.
     */
    private String gender;

    /**
     * Professional or major field of the student.
     */
    private String professional;
}