package com.example.tuitionpayment.alipay;

import static com.example.tuitionpayment.util.GlobalUrl.url;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;

import com.example.tuitionpayment.MainActivity;
import com.example.tuitionpayment.ManagePaymentActivity;
import com.example.tuitionpayment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AlipayOfSandbox extends AppCompatActivity {

    /**
     * Alipay App ID for payment operations.
     */
    public static final String APPID = "9021000123606477";

    /**
     * Alipay PID for account login authorization.
     */
    public static final String PID = "2088721005020122";

    /**
     * Alipay Target ID for account login authorization.
     */
    public static final String TARGET_ID = "";

    private RadioGroup paymentOptions;
    private Button confirmTransaction;
    private RadioButton alipayOption;
    private RadioButton wechatOption;
    private RadioButton applePayOption;
    private RadioButton yinlianPayOption;
    private RadioButton visaPayOption;
    SharedPreferences usInfo;
    private String userid;
    private String nickname;
    private String username;
    private String password;
    private LinearLayout paymentMethodsContainer;
    private List<PaymentMethod> paymentMethods = new ArrayList<>();
    private int selectedPaymentMethodId = -1;

    /**
     * RSA2 private key for signing requests (pkcs8 format).
     */
    public static final String RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC6S6S9UdUVnp2/wjO/IT9UQ68vP9etTDoGLRek4IpydASVNXV3/KSL3iXG+XaQwb5cJ4Q5bnzc674quxXJ2+JZhRtmyjIZ9BTCOu86g8znycUfNOkukNF1sFQ8+Vxpq3UTdS9ujMlZVks8HwG08CxgEDplJY0ZZ5R2jypft6FsybLlmB1EGw/sWqbu04mihIWmm1kWPz5mLfN7U9GHHc52kKJZGyJGFFalz3k8HMVbCiFUZcM7+JV+wcJNoqh1oDfD9xBM+ERLcBV3mtCAxqKIHxEuVnwwU0wde0AzCxiBiVvlVBNEEbGOICtUWuyL9+KsNctjQjihJSC9yU5ifsDZAgMBAAECggEBAK+XGqhi2C/veApE9lcg9DrtLNKhHx01ar9lgxKhnXyxQ5yLbbPxSfqXIQ5BFXLtWgosKOGk9UWZYX2PJsNDYD42SpSA4eY6e9JykAecMOcL86MtUNfWIccGsu9hravfspLrchCF3EY7IuCA7S/o7DqDMTcixLJ8QZ/Z4vui7iDFA0/w5camw4NfFG/2Pv865fSZUTM8NEpstrSKx2IZf/l5lfC3iVdjPz55L09W7yaFsi7PWQQFByZEzMU9axXEcRM21zXDL21bajDB2ptawDX4CP7s/kxHonO9bryrLIQF6ZNVj9v6Bo5hWn20QUuSUX40N1Juiht5h01uGSQphrECgYEA5/NzrzSBiDDScMdfchYAps8AR4a2l5KyDHqagS/ixanOa85/RJ1p4su8DY9sFLAEmE8h1E+udxF39/HzPS9cz0mj0IPKdTQK7aIgFeUW1VMZKMvgokF6MbnRG1u/RXLB2CJveQcRs+3OweQ2fB2quVkaqrI+IYUOX2pGJlZuhhUCgYEAzZxh8x3oa9HiWgou7nF/pHm00GatXksdTqj1CthF/NCD5rQhBwLqKB6OHmrykQYtUFcp4fhtmQxjwBz46MXITzgxvpvCbabNaIjc4ciifvRx6M2oa2s9neqqvixAWiZjTEWPLbZeD5YMeKfkntkiw7pmk85c1K4XYcQmURRDJLUCgYEAzjax3c1nb8ZAAYYvRAVttN+KFw0cXmtMIi4KGNqS2TrbSxs+KKluTvTkjoW5CDdpDmQj5lak/DSqnOQdZYMrFxsYKbRRlZucL6KYtf6DK0sCrjN71aUy1uyljJYn2b1puxb6VfZD9dxxmg2jpvrVIo2mWKW4+SILIicgkMr83fUCgYByZXK94EMDUig87nOEKNPav/hRBv+oeeRZcnm7ye7OpXjtsAS8MOv03GM4G0rQU358t7JOzniz8YfCSJxYbkpvgRV0NpcriOW6JoO2f3mhZwHFhD8TXPxQU1MiMd6wCbGvqvfpeBDHXTWdH9R8aNUFzzzpOaS+lAngVFD5RWvCDQKBgChwEE4zQBQ3PWcmv7whKaZv1A6K0MMhGfNrpIoi33gkXSo+xyCyP6wD+kvqGpPW/XmHP8r1qOdlhcMYkORaCIyTe+c/7qqIXx1m3tsP/WCDVkySP+zI51EzOZ+AjjK5uexhWa8sQ3qB/tZoYz2yncejIumlAJslC/M/GiHZcVNt";
    /**
     * RSA private key for signing requests (pkcs8 format).
     */
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private int id;
    private int notify;
    private String price;

    /**
     * Handler for processing messages related to payment and authorization results.
     * Handles cases for payment success, payment failure, and authorization results.
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    // Handle payment result
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);

                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // Check if resultStatus is 9000, indicating payment success
                    if (TextUtils.equals(resultStatus, "9000")) {

//                        showAlert(AlipayOfSandbox.this, getString(R.string.pay_success) + payResult);
                        Toast.makeText(getApplicationContext(),"Payment success",Toast.LENGTH_SHORT).show();
                        // Handle payment success in a background thread
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    LocalDateTime now = LocalDateTime.now();
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                    String formattedDateTime = now.format(formatter);
                                    OkHttpClient client = new OkHttpClient();
                                    // 创建 JSON 格式的请求体
                                    JSONObject json = new JSONObject();
                                    json.put("id", id);
                                    json.put("status", 1);
                                    json.put("paymentTime", formattedDateTime);
                                    json.put("paymentMethod", "Alipay");

                                    RequestBody body = RequestBody.create(
                                            json.toString(),
                                            MediaType.parse("application/json; charset=utf-8")
                                    );
                                    Request request = new Request.Builder()
                                            .url(url+"/tuitionInvoice")
                                            .post(body)
                                            .build();
                                    Response response = client.newCall(request).execute();
                                    String a = response.body().string();
                                    JSONObject jsonObject = new JSONObject(a);
                                    System.out.println(jsonObject);
                                    if (jsonObject.getString("code").equals("200")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(AlipayOfSandbox.this,"Payment Success!",Toast.LENGTH_SHORT).show();
                                                if(notify==0){
                                                    String info = "The tuition bill was successfully paid by Alipay！";
                                                    OkHttpClient client = new OkHttpClient();
                                                    // 创建 JSON 格式的请求体
                                                    JSONObject json = new JSONObject();
                                                    try {
                                                        json.put("id", id);
                                                        json.put("content", info);
                                                        json.put("uid", userid);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }


                                                    RequestBody body = RequestBody.create(
                                                            json.toString(),
                                                            MediaType.parse("application/json; charset=utf-8")
                                                    );
                                                    Request request = new Request.Builder()
                                                            .url(url+"/tuitionInvoice")
                                                            .post(body)
                                                            .build();
                                                    try {
                                                        Response response = client.newCall(request).execute();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                Intent intent =new Intent(AlipayOfSandbox.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }).start();


                    } else {

                        showAlert(AlipayOfSandbox.this, getString(R.string.pay_failed) + payResult);
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {

                        showAlert(AlipayOfSandbox.this, getString(R.string.auth_success) + authResult);
                    } else {

                        showAlert(AlipayOfSandbox.this, getString(R.string.auth_failed) + authResult);
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    /**
     * Initializes the sandbox environment and sets up payment options on activity creation.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay_of_sandbox);
        usInfo = getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        userid = usInfo.getString("userid", null);
        username = usInfo.getString("username", null);
        password = usInfo.getString("password", null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    json.put("username", username);


                    RequestBody body = RequestBody.create(
                            json.toString(),
                            MediaType.parse("application/json; charset=utf-8")
                    );

                    Request request = new Request.Builder()
                            .url(url+"/user/getOne")
                            .post(body)
                            .build();//创造http请求
                    Response response = client.newCall(request).execute();
                    String a = response.body().string();
                    JSONObject jsonObject = new JSONObject(a);
                    System.out.println(jsonObject);
                    if (jsonObject.getString("code").equals("200")) {

                        JSONArray dataArray = jsonObject.getJSONArray("data");

                        JSONObject jsonObject1 = dataArray.getJSONObject(0);
                        notify = jsonObject1.getInt("notify");


                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        Intent intent= getIntent();
        if (intent!=null){
            id = intent.getIntExtra("id",-1);
            System.out.println(id);
            price = String.valueOf(intent.getDoubleExtra("price",0));

        }
        paymentMethodsContainer = findViewById(R.id.paymentMethodsContainer);
        confirmTransaction = findViewById(R.id.confirmTransaction);
        loadPaymentMethods();
        confirmTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(AlipayOfSandbox.this);
                builder.setTitle("prompt");
                builder.setMessage("Are you sure you want to perform the operation?");
                builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handlePayment();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    /**
     * Loads the list of payment methods for the current user from the server.
     * Fetches the data in a background thread and updates the UI on the main thread.
     */
    private void loadPaymentMethods() {
        new Thread(() -> {
            try {
                // Initialize the HTTP client and prepare the request
                OkHttpClient client = new OkHttpClient();
                JSONObject json = new JSONObject();
                json.put("email", username); // Include the user's email in the request body

                RequestBody body = RequestBody.create(
                        json.toString(),
                        MediaType.parse("application/json; charset=utf-8")
                );
                Request request = new Request.Builder()
                        .url(url + "/payway/listByEmail") // Endpoint to fetch payment methods
                        .post(body)
                        .build();

                // Execute the HTTP request
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                // Check if the response indicates success
                if (jsonObject.getString("code").equals("200")) {
                    // Parse the payment methods from the response
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
     * Updates the payment methods view to display the list of available payment methods.
     * Each payment method is represented as a view in the container.
     */
    private void updatePaymentMethodsView() {
        paymentMethodsContainer.removeAllViews();
        for (PaymentMethod method : paymentMethods) {
            View paymentMethodView = LayoutInflater.from(this).inflate(R.layout.item_payment_method, paymentMethodsContainer, false);
            ImageView icon = paymentMethodView.findViewById(R.id.paymentIcon);
            TextView name = paymentMethodView.findViewById(R.id.paymentName);
            RadioButton radioButton = paymentMethodView.findViewById(R.id.paymentRadioButton);
            Button deletePaymentMethod = paymentMethodView.findViewById(R.id.deletePaymentMethod);
            deletePaymentMethod.setVisibility(View.GONE);
            // Set icon based on payment method name
            // You need to add proper drawables for each payment method
            icon.setImageResource(R.drawable.logo);
            name.setText(method.getName());
            if (method.isDefault()==1){
                radioButton.setChecked(true);
                selectedPaymentMethodId = method.getId();
            }

            radioButton.setOnClickListener(v -> {
                selectedPaymentMethodId = method.getId();
                updateRadioButtons();
            });


            paymentMethodsContainer.addView(paymentMethodView);
        }
    }

    /**
     * Updates the state of the radio buttons in the payment methods view.
     * Ensures only the selected payment method is marked as checked.
     */
    private void updateRadioButtons() {
        for (int i = 0; i < paymentMethodsContainer.getChildCount(); i++) {
            View child = paymentMethodsContainer.getChildAt(i);
            RadioButton radioButton = child.findViewById(R.id.paymentRadioButton);
            radioButton.setChecked(paymentMethods.get(i).getId() == selectedPaymentMethodId);
        }
    }

    /**
     * Handles the payment process for the selected payment method.
     * If no method is selected, prompts the user to choose one.
     * Performs the payment operation based on the selected method.
     */
    private void handlePayment() {
        // Check if a payment method is selected
        if (selectedPaymentMethodId == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return; // Return if no payment method is selected
        }

        // Retrieve the name of the selected payment method
        String selectedPaymentMethodName = null;
        for (PaymentMethod method : paymentMethods) {
            if (method.getId() == selectedPaymentMethodId) {
                selectedPaymentMethodName = method.getName();
                break;
            }
        }

        // Proceed with payment if a valid payment method is selected
        if (selectedPaymentMethodName != null) {

            Toast.makeText(this, "The payment method chosen: " + selectedPaymentMethodName, Toast.LENGTH_SHORT).show();
            if (selectedPaymentMethodName.equals("Alipay")){
                // Use Alipay for the payment
                payV2();
            }else {
                // Handle payment for other methods in a background thread
                String finalPaymentMethod = selectedPaymentMethodName;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        String formattedDateTime = now.format(formatter);

                        // Create a JSON object for the payment request
                        JSONObject json = new JSONObject();
                        try {
                            json.put("id", id);
                            json.put("status", 1);
                            json.put("paymentTime", formattedDateTime);
                            json.put("paymentMethod", finalPaymentMethod);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }

                        // Notify the user of the successful payment
                        String info = "Use " + finalPaymentMethod + " To Pay Tuition Fees Successful！";
                        RequestBody body = RequestBody.create(
                                json.toString(),
                                MediaType.parse("application/json; charset=utf-8")
                        );

                        Request request = new Request.Builder()
                                .url(url + "/tuitionInvoice")
                                .post(body)
                                .build();

                        try {
                            Response response = client.newCall(request).execute();
                            String responseBody = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseBody);
                            System.out.println(jsonObject);

                            if (jsonObject.getString("code").equals("200")) {
                                // Create a notification request
                                JSONObject json1 = new JSONObject();
                                try {
                                    json1.put("id", id);
                                    json1.put("content", info);
                                    json1.put("uid", userid);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    return;
                                }

                                RequestBody body1 = RequestBody.create(
                                        json1.toString(),
                                        MediaType.parse("application/json; charset=utf-8")
                                );
                                Request request1 = new Request.Builder()
                                        .url(url + "/notify")
                                        .post(body1)
                                        .build();

                                try {
                                    Response response1 = client.newCall(request1).execute();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                // Update UI on the main thread
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Toast.makeText(AlipayOfSandbox.this, "Payment Success!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AlipayOfSandbox.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        } else {
            Toast.makeText(this, "No corresponding payment method found", Toast.LENGTH_SHORT).show();
        }
    }



    public void payV2() {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            showAlert(this, getString(R.string.error_missing_appid_rsa_private));
            return;
        }

        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2,price);

        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(AlipayOfSandbox.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * Demonstrates Alipay account authorization functionality.
     * Ensures required parameters are provided and initiates the authorization process.
     *
     * @param v The view that triggers this method.
     */
    public void authV2(View v) {
        // Check if essential parameters for authorization are present
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
                || TextUtils.isEmpty(TARGET_ID)) {
            showAlert(this, getString(R.string.error_auth_missing_partner_appid_rsa_private_target_id));
            return;
        }

        // Determine whether to use RSA2 or RSA for signing
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        // Generate the signature for the authorization request
        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        final String authInfo = info + "&" + sign;

        // Runnable to handle the authorization in a background thread
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // Construct an AuthTask object
                AuthTask authTask = new AuthTask(AlipayOfSandbox.this);
                // Send the result back to the main thread via a handler
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // Authorization process must be performed asynchronously
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    /**
     * Retrieves and displays the version number of the Alipay SDK.
     *
     * @param v The view that triggers this method.
     */
    public void showSdkVersion(View v) {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        showAlert(this, getString(R.string.alipay_sdk_version_is) + version);
    }

    /**
     * Demonstrates how to convert H5 web-based payment into Alipay App payment.
     * Launches a WebView to display the payment page, or opens a native Alipay app if available.
     *
     * @param v The view that triggers this method.
     */
    public void h5Pay(View v) {
        WebView.setWebContentsDebuggingEnabled(true);
        Intent intent = new Intent(this, AlipayOfSandbox.class);
        Bundle extras = new Bundle();

        // Example URL for a web-based payment page
        String url = "https://m.taobao.com";
        extras.putString("url", url);
        intent.putExtras(extras);
        startActivity(intent);
    }

    private static void showAlert(Context ctx, String info) {
        showAlert(ctx, info, null);
    }

    private static void showAlert(Context ctx, String info, DialogInterface.OnDismissListener onDismiss) {
        new AlertDialog.Builder(ctx)
                .setMessage(info)
                .setPositiveButton(R.string.confirm, null)
                .setOnDismissListener(onDismiss)
                .show();
    }

    private static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

    private static String bundleToString(Bundle bundle) {
        if (bundle == null) {
            return "null";
        }
        final StringBuilder sb = new StringBuilder();
        for (String key: bundle.keySet()) {
            sb.append(key).append("=>").append(bundle.get(key)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Represents a payment method with details such as ID, name, and whether it is set as default.
     */
    private static class PaymentMethod {
        /**
         * Unique identifier for the payment method.
         */
        private int id;

        /**
         * Name of the payment method (e.g., "Alipay", "Visa").
         */
        private String name;

        /**
         * Indicates if this payment method is the default one.
         * Typically, 1 represents default, and 0 represents non-default.
         */
        private int isDefault;

        /**
         * Constructs a new {@code PaymentMethod} instance with the specified attributes.
         *
         * @param id        Unique identifier for the payment method.
         * @param name      Name of the payment method.
         * @param isDefault Default status of the payment method (1 for default, 0 for non-default).
         */
        public PaymentMethod(int id, String name, int isDefault) {
            this.id = id;
            this.name = name;
            this.isDefault = isDefault;
        }

        /**
         * Retrieves the unique identifier of the payment method.
         *
         * @return The payment method ID.
         */
        public int getId() {
            return id;
        }

        /**
         * Retrieves the name of the payment method.
         *
         * @return The payment method name.
         */
        public String getName() {
            return name;
        }

        /**
         * Checks whether the payment method is set as the default.
         *
         * @return An integer representing the default status (1 for default, 0 for non-default).
         */
        public int isDefault() {
            return isDefault;
        }
    }
}

