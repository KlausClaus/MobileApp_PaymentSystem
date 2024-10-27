package com.example.tuitionpayment.entity;

import java.io.Serializable;

public class Item implements Serializable {
    private Integer id; // 唯一标识符
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
    private Integer status;

    public Item(int id, String studentName, String studentEmail, String major, String academicYear, double totalFee, double tuitionFee, double accommodationFee, double bookFee, double materialFee, double activityFee, double examFee, String paymentMethod, double paymentAmount, String createdTime, String paymentTime, int status) {
    }

    public Item() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public Double getTuitionFee() {
        return tuitionFee;
    }

    public void setTuitionFee(Double tuitionFee) {
        this.tuitionFee = tuitionFee;
    }

    public Double getAccommodationFee() {
        return accommodationFee;
    }

    public void setAccommodationFee(Double accommodationFee) {
        this.accommodationFee = accommodationFee;
    }

    public Double getBookFee() {
        return bookFee;
    }

    public void setBookFee(Double bookFee) {
        this.bookFee = bookFee;
    }

    public Double getMaterialFee() {
        return materialFee;
    }

    public void setMaterialFee(Double materialFee) {
        this.materialFee = materialFee;
    }

    public Double getActivityFee() {
        return activityFee;
    }

    public void setActivityFee(Double activityFee) {
        this.activityFee = activityFee;
    }

    public Double getExamFee() {
        return examFee;
    }

    public void setExamFee(Double examFee) {
        this.examFee = examFee;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Item(Integer id, String studentName, String studentEmail, String major, String academicYear, Double totalFee, Double tuitionFee, Double accommodationFee, Double bookFee, Double materialFee, Double activityFee, Double examFee, String paymentMethod, Double paymentAmount, String createdTime, String paymentTime) {
        this.id = id;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.major = major;
        this.academicYear = academicYear;
        this.totalFee = totalFee;
        this.tuitionFee = tuitionFee;
        this.accommodationFee = accommodationFee;
        this.bookFee = bookFee;
        this.materialFee = materialFee;
        this.activityFee = activityFee;
        this.examFee = examFee;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.createdTime = createdTime;
        this.paymentTime = paymentTime;
    }

    public Item(Integer id, String studentName, String studentEmail, String major, String academicYear, Double totalFee, Double tuitionFee, Double accommodationFee, Double bookFee, Double materialFee, Double activityFee, Double examFee, String paymentMethod, Double paymentAmount, String createdTime, String paymentTime, Integer status) {
        this.id = id;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.major = major;
        this.academicYear = academicYear;
        this.totalFee = totalFee;
        this.tuitionFee = tuitionFee;
        this.accommodationFee = accommodationFee;
        this.bookFee = bookFee;
        this.materialFee = materialFee;
        this.activityFee = activityFee;
        this.examFee = examFee;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.createdTime = createdTime;
        this.paymentTime = paymentTime;
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", studentName='" + studentName + '\'' +
                ", studentEmail='" + studentEmail + '\'' +
                ", major='" + major + '\'' +
                ", academicYear='" + academicYear + '\'' +
                ", totalFee=" + totalFee +
                ", tuitionFee=" + tuitionFee +
                ", accommodationFee=" + accommodationFee +
                ", bookFee=" + bookFee +
                ", materialFee=" + materialFee +
                ", activityFee=" + activityFee +
                ", examFee=" + examFee +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentAmount=" + paymentAmount +
                ", createdTime='" + createdTime + '\'' +
                ", paymentTime='" + paymentTime + '\'' +
                ", status=" + status +
                '}';
    }
}
