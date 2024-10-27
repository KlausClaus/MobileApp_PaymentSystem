package com.example.payment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "tuition") // 对应数据库表名
public class TuitionInvoice {
    @TableId(type = IdType.AUTO) // 主键，自增
    private Integer id; // 唯一标识符
    private Integer status;
    private String studentName; // 学生姓名
    private String studentEmail; // 邮箱
    private String major; // 专业
    private String academicYear; // 学年或学期
    private Double totalFee; // 学费总额
    private Double tuitionFee; // 学费
    private Double accommodationFee; // 住宿费（可选）
    private Double bookFee; // 书本费（可选）
    private Double materialFee; // 材料费（可选）
    private Double activityFee; // 课外活动费（可选）
    private Double examFee; // 考试费（可选）
    private String paymentMethod; // 付款方式
    private Double paymentAmount; // 付款金额
    private String createdTime; // 创建时间
    private String paymentTime; // 付款时间（可选）
}
