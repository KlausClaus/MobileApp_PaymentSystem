package com.example.payment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Entity representing a tuition invoice.
 * Maps to the "tuition" table in the database.
 */
@Data
@TableName(value = "tuition")
public class TuitionInvoice {
    /**
     * Unique identifier for the tuition invoice.
     * Automatically generated primary key.
     */
    @TableId(type = IdType.AUTO) // Primary key, auto-increment
    private Integer id;

    /**
     * Status of the tuition invoice (e.g., paid, pending).
     */
    private Integer status;

    /**
     * Name of the student associated with the tuition invoice.
     */
    private String studentName;

    /**
     * Email of the student associated with the tuition invoice.
     */
    private String studentEmail;

    /**
     * Major of the student.
     */
    private String major;

    /**
     * Academic year or semester for the tuition invoice.
     */
    private String academicYear;

    /**
     * Total fee for the tuition invoice.
     */
    private Double totalFee;

    /**
     * Tuition fee for the academic period.
     */
    private Double tuitionFee;

    /**
     * Accommodation fee (optional).
     */
    private Double accommodationFee;

    /**
     * Book fee (optional).
     */
    private Double bookFee;

    /**
     * Material fee (optional).
     */
    private Double materialFee;

    /**
     * Activity fee for extracurricular activities (optional).
     */
    private Double activityFee;

    /**
     * Exam fee (optional).
     */
    private Double examFee;

    /**
     * Payment method used for the tuition invoice.
     */
    private String paymentMethod;

    /**
     * Amount paid for the tuition invoice.
     */
    private Double paymentAmount;

    /**
     * Time when the tuition invoice was created.
     */
    private String createdTime;

    /**
     * Time when the payment was made (optional).
     */
    private String paymentTime;
}
