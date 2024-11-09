package com.example.tuitionpayment;

import static com.example.tuitionpayment.util.GlobalUrl.url;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ManagePaymentActivity extends AppCompatActivity {

    private LinearLayout paymentMethodsContainer;
    private Button addPaymentMethod;
    private Button confirmTransaction;
    private SharedPreferences usInfo;
    private String username;
    private List<PaymentMethod> paymentMethods = new ArrayList<>();
    private int selectedPaymentMethodId = -1;
    private int chushiId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_payment);

        usInfo = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        username = usInfo.getString("username", null);

        paymentMethodsContainer = findViewById(R.id.paymentMethodsContainer);
        addPaymentMethod = findViewById(R.id.addPaymentMethod);
        confirmTransaction = findViewById(R.id.confirmTransaction);

        loadPaymentMethods();

        addPaymentMethod.setOnClickListener(v -> showAddPaymentMethodDialog());
        confirmTransaction.setOnClickListener(v -> savePaymentMethods());
    }

    private void loadPaymentMethods() {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                JSONObject json = new JSONObject();
                json.put("email", username);

                RequestBody body = RequestBody.create(
                        json.toString(),
                        MediaType.parse("application/json; charset=utf-8")
                );
                Request request = new Request.Builder()
                        .url(url + "/payway/listByEmail")
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                if (jsonObject.getString("code").equals("200")) {
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject paymentObject = dataArray.getJSONObject(i);
                        PaymentMethod paymentMethod = new PaymentMethod(
                                paymentObject.getInt("id"),
                                paymentObject.getString("payway"),
                                paymentObject.getInt("isDefault")
                        );
                        paymentMethods.add(paymentMethod);
                    }
                    runOnUiThread(this::updatePaymentMethodsView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void updatePaymentMethodsView() {
        paymentMethodsContainer.removeAllViews();
        for (PaymentMethod method : paymentMethods) {
            View paymentMethodView = LayoutInflater.from(this).inflate(R.layout.item_payment_method, paymentMethodsContainer, false);
            ImageView icon = paymentMethodView.findViewById(R.id.paymentIcon);
            TextView name = paymentMethodView.findViewById(R.id.paymentName);
            RadioButton radioButton = paymentMethodView.findViewById(R.id.paymentRadioButton);
            Button deleteButton = paymentMethodView.findViewById(R.id.deletePaymentMethod);
    
            // Set the payment method name
            name.setText(method.getName());
    
            // Set the appropriate icon based on the payment method name
            int iconResId = getIconResourceIdForPaymentMethod(method.getName());
            if (iconResId != 0) {
                icon.setImageResource(iconResId);
            }
    
            // Set the RadioButton state
            radioButton.setChecked(method.isDefault() == 1);
    
            // Handle RadioButton click
            radioButton.setOnClickListener(v -> {
                selectedPaymentMethodId = method.getId();
                updateRadioButtons();
            });
    
            // Handle delete button click
            deleteButton.setOnClickListener(v -> deletePaymentMethod(method));
    
            // Add the payment method view to the container
            paymentMethodsContainer.addView(paymentMethodView);
        }
    }

    private int getIconResourceIdForPaymentMethod(String methodName) {
        switch (methodName.toLowerCase()) {
            case "alipay":
                return R.drawable.alipay_logo;
            case "wechat pay":
                return R.drawable.wechat_logo;
            case "apple pay":
                return R.drawable.applepay_logo;
            case "unionpay":
                return R.drawable.yinlian_logo;
            case "visa":
                return R.drawable.visa_logo;
            default:
                return 0; // 返回 0 或者返回一个默认图标的资源 ID
        }
    }
    


    private void updateRadioButtons() {
        for (int i = 0; i < paymentMethodsContainer.getChildCount(); i++) {
            View child = paymentMethodsContainer.getChildAt(i);
            RadioButton radioButton = child.findViewById(R.id.paymentRadioButton);
            radioButton.setChecked(paymentMethods.get(i).getId() == selectedPaymentMethodId);
        }
    }

    private void showAddPaymentMethodDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Payment Method");

        // 设置输入框
        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String newPaymentMethod = input.getText().toString().trim();
            if (!newPaymentMethod.isEmpty()) {
                addNewPaymentMethod(newPaymentMethod);
            } else {
                Toast.makeText(ManagePaymentActivity.this, "The payment method cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    private void addNewPaymentMethod(String paymentMethodName) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                JSONObject json = new JSONObject();
                json.put("email", username);
                json.put("payway", paymentMethodName);
                json.put("isDefault", 0);
    
                RequestBody body = RequestBody.create(
                        json.toString(),
                        MediaType.parse("application/json; charset=utf-8")
                );
                Request request = new Request.Builder()
                        .url(url + "/payway")
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
    
                if (jsonObject.getString("code").equals("200")) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    int newMethodId = data.getInt("id");
    
                    // 检查是否已有相同支付方式，如果有，将其设为默认
                    boolean alreadyExists = false;
                    for (PaymentMethod method : paymentMethods) {
                        if (method.getName().equalsIgnoreCase(paymentMethodName)) {
                            alreadyExists = true;
                            selectedPaymentMethodId = method.getId(); // 设置为已存在的支付方式ID
                            break;
                        }
                    }
    
                    if (!alreadyExists) {
                        // 如果是新支付方式，则添加到列表
                        PaymentMethod newMethod = new PaymentMethod(
                                newMethodId,
                                data.getString("payway"),
                                data.getInt("isDefault")
                        );
                        paymentMethods.add(newMethod);
                    } else {
                        // 如果已存在，只设置为默认
                        selectedPaymentMethodId = newMethodId;
                    }
    
                    // 更新界面和保存设置为默认
                    runOnUiThread(() -> {
                        updateRadioButtons(); // 更新界面上的选中状态
                        savePaymentMethods(); // 保存设置，将该支付方式设为默认
                        updatePaymentMethodsView(); // 刷新视图，确保显示5个支付方式
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    

    private void deletePaymentMethod(PaymentMethod method) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url + "/payway/del/" + method.getId())
                        .delete()
                        .build();
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                if (jsonObject.getString("code").equals("200")) {
                    paymentMethods.remove(method);
                    runOnUiThread(this::updatePaymentMethodsView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void savePaymentMethods() {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                if (chushiId != selectedPaymentMethodId) {
                    // 将 chushiId 对应的 isDefault 置为 0
                    JSONObject updateDefaultJson = new JSONObject();
                    updateDefaultJson.put("id", chushiId);
                    updateDefaultJson.put("isDefault", 0);

                    RequestBody updateDefaultBody = RequestBody.create(
                            updateDefaultJson.toString(),
                            MediaType.parse("application/json; charset=utf-8")
                    );
                    Request updateDefaultRequest = new Request.Builder()
                            .url(url + "/payway")
                            .post(updateDefaultBody)
                            .build();
                    client.newCall(updateDefaultRequest).execute(); // 执行更新请求
                }

                // 更新 selectedPaymentMethodId 对应的 isDefault 置为 1
                JSONObject setDefaultJson = new JSONObject();
                setDefaultJson.put("email", username);
                setDefaultJson.put("isDefault", 1);
                setDefaultJson.put("id", selectedPaymentMethodId);

                RequestBody setDefaultBody = RequestBody.create(
                        setDefaultJson.toString(),
                        MediaType.parse("application/json; charset=utf-8")
                );
                Request setDefaultRequest = new Request.Builder()
                        .url(url + "/payway")
                        .post(setDefaultBody)
                        .build();
                Response response = client.newCall(setDefaultRequest).execute(); // 执行设置请求

                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                if (jsonObject.getString("code").equals("200")) {
                    runOnUiThread(() -> {
                        Toast.makeText(ManagePaymentActivity.this, "Settings saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ManagePaymentActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    private static class PaymentMethod {
        private int id;
        private String name;
        private int isDefault;

        public PaymentMethod(int id, String name, int isDefault) {
            this.id = id;
            this.name = name;
            this.isDefault = isDefault;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int isDefault() {
            return isDefault;
        }
    }
}