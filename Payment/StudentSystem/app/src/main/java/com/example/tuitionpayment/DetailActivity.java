package com.example.tuitionpayment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionpayment.alipay.AlipayOfSandbox;
import com.example.tuitionpayment.entity.Item;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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
        Button confirm = findViewById(R.id.btn_confirm_payment);

        tuitionFee.setText("$" + String.valueOf(item.getTuitionFee()));
        accommodationFee.setText(item.getAccommodationFee() != null ? "$" + String.valueOf(item.getAccommodationFee()) : "N/A");
        bookFee.setText(item.getBookFee() != null ? "$" + String.valueOf(item.getBookFee()) : "N/A");
        materialFee.setText(item.getMaterialFee() != null ? "$" + String.valueOf(item.getMaterialFee()) : "N/A");
        activityFee.setText(item.getActivityFee() != null ? "$" + String.valueOf(item.getActivityFee()) : "N/A");
        examFee.setText(item.getExamFee() != null ? "$" + String.valueOf(item.getExamFee()) : "N/A");
        totalFee.setText(item.getTotalFee() != null ? "$" + String.valueOf(item.getTotalFee()) : "N/A");


        // 显示付款信息
//        TextView paymentAmount = findViewById(R.id.tv_payment_amount);
        TextView paymentStatus = findViewById(R.id.tv_payment_status);

//        paymentAmount.setText(String.valueOf(item.getPaymentAmount()));
        paymentStatus.setText(item.getStatus() != 0 ? "paid" : "non-payment");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setTitle("提示");
                builder.setMessage("确定要执行操作吗？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 在这里执行确认后的操作
                        Intent intent1 = new Intent(DetailActivity.this, AlipayOfSandbox.class);
                        intent1.putExtra("id",item.getId());
                        intent1.putExtra("price",item.getTotalFee());
                        startActivity(intent1);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DetailActivity.this,"取消",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }
}
