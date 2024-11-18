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

/**
 * Activity class that displays the details of a tuition item, including fees and student information,
 * and allows the user to confirm payment.
 */
public class DetailActivity extends AppCompatActivity {

    /**
     * Called when the activity is starting. This method initializes the UI components
     * and displays the details of the selected tuition item.
     *
     * @param savedInstanceState the previously saved state of the activity, if any
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Retrieve the passed Item object
        Item item = (Item) getIntent().getSerializableExtra("item");

        // Display student information
        TextView studentName = findViewById(R.id.tv_student_name);
        TextView studentEmail = findViewById(R.id.tv_student_email);
        TextView major = findViewById(R.id.tv_major);
        TextView academicYear = findViewById(R.id.tv_academic_year);

        studentName.setText(item.getStudentName());
        studentEmail.setText(item.getStudentEmail());
        major.setText(item.getMajor());
        academicYear.setText(item.getAcademicYear());

        // Display fee details
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


        // Display payment status
//        TextView paymentAmount = findViewById(R.id.tv_payment_amount);
        TextView paymentStatus = findViewById(R.id.tv_payment_status);

//        paymentAmount.setText(String.valueOf(item.getPaymentAmount()));
        paymentStatus.setText(item.getStatus() != 0 ? "paid" : "non-payment");

        // Set up click listener for the confirm payment button
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setTitle("prompt");
                builder.setMessage("Are you sure you want to perform the operation?");
                builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent1 = new Intent(DetailActivity.this, AlipayOfSandbox.class);
                        intent1.putExtra("id",item.getId());
                        intent1.putExtra("price",item.getTotalFee());
                        startActivity(intent1);
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DetailActivity.this,"cancel",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }
}
