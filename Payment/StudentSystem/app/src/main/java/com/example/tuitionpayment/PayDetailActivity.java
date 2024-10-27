package com.example.tuitionpayment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.tuitionpayment.entity.Item;

public class PayDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_detail);

        // 获取传递的 Item 对象
        Item item = (Item) getIntent().getSerializableExtra("item");

        // 显示学生信息
        TextView studentName = findViewById(R.id.tv_student_name);
        TextView studentEmail = findViewById(R.id.tv_student_email);
        TextView major = findViewById(R.id.tv_major);
        TextView academicYear = findViewById(R.id.tv_academic_year);

        studentName.setText(item.getStudentName());
        studentEmail.setText(item.getStudentEmail());
        major.setText(item.getMajor());
        academicYear.setText(item.getAcademicYear());

        // 显示费用明细
        TextView tuitionFee = findViewById(R.id.tv_tuition_fee);
        TextView accommodationFee = findViewById(R.id.tv_accommodation_fee);
        TextView bookFee = findViewById(R.id.tv_book_fee);
        TextView materialFee = findViewById(R.id.tv_material_fee);
        TextView activityFee = findViewById(R.id.tv_activity_fee);
        TextView examFee = findViewById(R.id.tv_exam_fee);
        TextView totalFee = findViewById(R.id.tv_total_fee_value);
        TextView time = findViewById(R.id.tv_time);

        tuitionFee.setText("$" + String.valueOf(item.getTuitionFee()));
        accommodationFee.setText(item.getAccommodationFee() != null ? "$" + String.valueOf(item.getAccommodationFee()) : "N/A");
        bookFee.setText(item.getBookFee() != null ? "$" + String.valueOf(item.getBookFee()) : "N/A");
        materialFee.setText(item.getMaterialFee() != null ? "$" + String.valueOf(item.getMaterialFee()) : "N/A");
        activityFee.setText(item.getActivityFee() != null ? "$" + String.valueOf(item.getActivityFee()) : "N/A");
        examFee.setText(item.getExamFee() != null ? "$" + String.valueOf(item.getExamFee()) : "N/A");
        totalFee.setText(item.getTotalFee() != null ? "$" + String.valueOf(item.getTotalFee()) : "N/A");
        time.setText(item.getPaymentTime());

        // 显示付款信息
        TextView paymentAmount = findViewById(R.id.tv_payment_amount);
        TextView paymentStatus = findViewById(R.id.tv_payment_status);

        paymentAmount.setText(String.valueOf(item.getPaymentAmount()));
        paymentStatus.setText(item.getStatus() != 0 ? "Complete Payment" : "Non Payment");
    }
}