package com.example.tuitionpayment.entity;

import java.io.Serializable;

/**
 * Represents an item containing details about a student's tuition and related fees.
 * This class is used for storing and transferring information related to tuition invoices.
 */
public class Item implements Serializable {
    /**
     * Unique identifier for the item.
     */
    private Integer id;

    /**
     * Name of the student.
     */
    private String studentName;

    /**
     * Email address of the student.
     */
    private String studentEmail;

    /**
     * Major or field of study of the student.
     */
    private String major;

    /**
     * Academic year or term.
     */
    private String academicYear;

    /**
     * Total fee amount for the tuition.
     */
    private Double totalFee;

    /**
     * Tuition fee amount.
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
     * Extracurricular activity fee (optional).
     */
    private Double activityFee;

    /**
     * Examination fee (optional).
     */
    private Double examFee;

    /**
     * Payment method used for the tuition.
     */
    private String paymentMethod;

    /**
     * Timestamp of when the item was created.
     */
    private String createdTime;

    /**
     * Timestamp of when the payment was made (optional).
     */
    private String paymentTime;

    /**
     * Status of the item (e.g., paid or unpaid).
     */
    private Integer status;

    /**
     * Constructor for creating an item with detailed fields.
     *
     * @param id               Unique identifier for the item.
     * @param studentName      Name of the student.
     * @param studentEmail     Email address of the student.
     * @param major            Major or field of study of the student.
     * @param academicYear     Academic year or term.
     * @param totalFee         Total fee amount for the tuition.
     * @param tuitionFee       Tuition fee amount.
     * @param accommodationFee Accommodation fee (optional).
     * @param bookFee          Book fee (optional).
     * @param materialFee      Material fee (optional).
     * @param activityFee      Extracurricular activity fee (optional).
     * @param examFee          Examination fee (optional).
     * @param paymentMethod    Payment method used for the tuition.
     * @param createdTime      Timestamp of when the item was created.
     * @param paymentTime      Timestamp of when the payment was made (optional).
     * @param status           Status of the item (e.g., paid or unpaid).
     */
    public Item(int id, String studentName, String studentEmail, String major, String academicYear, double totalFee, double tuitionFee, double accommodationFee, double bookFee, double materialFee, double activityFee, double examFee, String paymentMethod, String createdTime, String paymentTime, int status) {
    }

    /**
     * Default constructor.
     */
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

    public Item(Integer id, String studentName, String studentEmail, String major, String academicYear, Double totalFee, Double tuitionFee, Double accommodationFee, Double bookFee, Double materialFee, Double activityFee, Double examFee, String paymentMethod, String createdTime, String paymentTime) {
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
        this.createdTime = createdTime;
        this.paymentTime = paymentTime;
    }

    public Item(Integer id, String studentName, String studentEmail, String major, String academicYear, Double totalFee, Double tuitionFee, Double accommodationFee, Double bookFee, Double materialFee, Double activityFee, Double examFee, String paymentMethod, String createdTime, String paymentTime, Integer status) {
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
                ", createdTime='" + createdTime + '\'' +
                ", paymentTime='" + paymentTime + '\'' +
                ", status=" + status +
                '}';
    }
}
