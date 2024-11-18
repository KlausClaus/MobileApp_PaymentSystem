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

/**
 * Activity for managing payment methods. Allows users to add, delete, view, and save payment methods.
 */
public class ManagePaymentActivity extends AppCompatActivity {

    private LinearLayout paymentMethodsContainer;
    private Button addPaymentMethod;
    private Button confirmTransaction;
    private SharedPreferences usInfo;
    private String username;
    private List<PaymentMethod> paymentMethods = new ArrayList<>();
    private int selectedPaymentMethodId = -1;
    private int chushiId = -1;

    /**
     * Called when the activity is starting. Initializes views and loads payment methods.
     *
     * @param savedInstanceState the previously saved state of the activity, if any
     */
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

    /**
     * Loads payment methods from the server and updates the UI.
     */
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

    /**
     * Updates the UI to display the current list of payment methods.
     */
    private void updatePaymentMethodsView() {
        paymentMethodsContainer.removeAllViews();
        for (PaymentMethod method : paymentMethods) {
            View paymentMethodView = LayoutInflater.from(this).inflate(R.layout.item_payment_method, paymentMethodsContainer, false);
            ImageView icon = paymentMethodView.findViewById(R.id.paymentIcon);
            TextView name = paymentMethodView.findViewById(R.id.paymentName);
            RadioButton radioButton = paymentMethodView.findViewById(R.id.paymentRadioButton);
            Button deleteButton = paymentMethodView.findViewById(R.id.deletePaymentMethod);

            // Set icon based on payment method name
            icon.setImageResource(getIconResourceIdForPaymentMethod(method.getName()));

            name.setText(method.getName());
            if (method.isDefault()==1){
                chushiId = method.getId();
                radioButton.setChecked(true);

            }

            radioButton.setOnClickListener(v -> {
                selectedPaymentMethodId = method.getId();
                updateRadioButtons();
            });

            deleteButton.setOnClickListener(v -> {
                // Show confirmation dialog before deleting
                AlertDialog.Builder builder = new AlertDialog.Builder(ManagePaymentActivity.this);
                builder.setTitle("Delete Payment Method");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    // Call the method to delete the payment method
                    deletePaymentMethod(method);
                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                builder.create().show();
            });

            paymentMethodsContainer.addView(paymentMethodView);
        }
    }

    /**
     * Returns the drawable resource ID for a given payment method name.
     *
     * @param methodName the name of the payment method
     * @return the drawable resource ID, or 0 if not found
     */
    private int getIconResourceIdForPaymentMethod(String methodName) {
        switch (methodName.toLowerCase()) {
            case "alipay":
                return R.drawable.alipay_logo;
            case "wechat":
                return R.drawable.wechat_logo;
            case "applepay":
                return R.drawable.applepay_logo;
            case "unionpay":
                return R.drawable.yinlian_logo;
            case "visa":
                return R.drawable.visa_logo;
            default:
                return 0;
        }
    }

    /**
     * Updates the selection state of all radio buttons in the payment methods list.
     */
    private void updateRadioButtons() {
        for (int i = 0; i < paymentMethodsContainer.getChildCount(); i++) {
            View child = paymentMethodsContainer.getChildAt(i);
            RadioButton radioButton = child.findViewById(R.id.paymentRadioButton);
            radioButton.setChecked(paymentMethods.get(i).getId() == selectedPaymentMethodId);
        }
    }

    /**
     * Displays a dialog for adding a new payment method.
     */
    private void showAddPaymentMethodDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Payment Method");

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

    /**
     * Sends a request to add a new payment method to the server and updates the UI.
     *
     * @param paymentMethodName the name of the new payment method
     */
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
                    PaymentMethod newMethod = new PaymentMethod(
                            data.getInt("id"),
                            data.getString("payway"),
                            data.getInt("isDefault")
                    );
                    paymentMethods.add(newMethod);
                    runOnUiThread(this::updatePaymentMethodsView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Sends a request to delete a payment method and updates the UI.
     *
     * @param method the payment method to be deleted
     */
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

    /**
     * Sends requests to update the default payment method on the server.
     */
    private void savePaymentMethods() {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                if (chushiId != selectedPaymentMethodId) {

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
                    client.newCall(updateDefaultRequest).execute();
                }

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
                Response response = client.newCall(setDefaultRequest).execute();

                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                if (jsonObject.getString("code").equals("200")) {
                    runOnUiThread(() -> {
                        Toast.makeText(ManagePaymentActivity.this, "Settings saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ManagePaymentActivity.this, ManagePaymentActivity.class);
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