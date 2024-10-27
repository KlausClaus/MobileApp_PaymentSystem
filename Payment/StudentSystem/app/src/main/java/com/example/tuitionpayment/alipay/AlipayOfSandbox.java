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
     * 用于支付宝支付业务的入参 app_id。
     */
    public static final String APPID = "9021000123606477";

    /**
     * 用于支付宝账户登录授权业务的入参 pid。
     */
    public static final String PID = "2088721005020122";

    /**
     * 用于支付宝账户登录授权业务的入参 target_id。
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
     *  pkcs8 格式的商户私钥。
     *
     * 	如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个，如果两个都设置了，本 Demo 将优先
     * 	使用 RSA2_PRIVATE。RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议商户使用
     * 	RSA2_PRIVATE。
     *
     * 	建议使用支付宝提供的公私钥生成工具生成和获取 RSA2_PRIVATE。
     * 	工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC6S6S9UdUVnp2/wjO/IT9UQ68vP9etTDoGLRek4IpydASVNXV3/KSL3iXG+XaQwb5cJ4Q5bnzc674quxXJ2+JZhRtmyjIZ9BTCOu86g8znycUfNOkukNF1sFQ8+Vxpq3UTdS9ujMlZVks8HwG08CxgEDplJY0ZZ5R2jypft6FsybLlmB1EGw/sWqbu04mihIWmm1kWPz5mLfN7U9GHHc52kKJZGyJGFFalz3k8HMVbCiFUZcM7+JV+wcJNoqh1oDfD9xBM+ERLcBV3mtCAxqKIHxEuVnwwU0wde0AzCxiBiVvlVBNEEbGOICtUWuyL9+KsNctjQjihJSC9yU5ifsDZAgMBAAECggEBAK+XGqhi2C/veApE9lcg9DrtLNKhHx01ar9lgxKhnXyxQ5yLbbPxSfqXIQ5BFXLtWgosKOGk9UWZYX2PJsNDYD42SpSA4eY6e9JykAecMOcL86MtUNfWIccGsu9hravfspLrchCF3EY7IuCA7S/o7DqDMTcixLJ8QZ/Z4vui7iDFA0/w5camw4NfFG/2Pv865fSZUTM8NEpstrSKx2IZf/l5lfC3iVdjPz55L09W7yaFsi7PWQQFByZEzMU9axXEcRM21zXDL21bajDB2ptawDX4CP7s/kxHonO9bryrLIQF6ZNVj9v6Bo5hWn20QUuSUX40N1Juiht5h01uGSQphrECgYEA5/NzrzSBiDDScMdfchYAps8AR4a2l5KyDHqagS/ixanOa85/RJ1p4su8DY9sFLAEmE8h1E+udxF39/HzPS9cz0mj0IPKdTQK7aIgFeUW1VMZKMvgokF6MbnRG1u/RXLB2CJveQcRs+3OweQ2fB2quVkaqrI+IYUOX2pGJlZuhhUCgYEAzZxh8x3oa9HiWgou7nF/pHm00GatXksdTqj1CthF/NCD5rQhBwLqKB6OHmrykQYtUFcp4fhtmQxjwBz46MXITzgxvpvCbabNaIjc4ciifvRx6M2oa2s9neqqvixAWiZjTEWPLbZeD5YMeKfkntkiw7pmk85c1K4XYcQmURRDJLUCgYEAzjax3c1nb8ZAAYYvRAVttN+KFw0cXmtMIi4KGNqS2TrbSxs+KKluTvTkjoW5CDdpDmQj5lak/DSqnOQdZYMrFxsYKbRRlZucL6KYtf6DK0sCrjN71aUy1uyljJYn2b1puxb6VfZD9dxxmg2jpvrVIo2mWKW4+SILIicgkMr83fUCgYByZXK94EMDUig87nOEKNPav/hRBv+oeeRZcnm7ye7OpXjtsAS8MOv03GM4G0rQU358t7JOzniz8YfCSJxYbkpvgRV0NpcriOW6JoO2f3mhZwHFhD8TXPxQU1MiMd6wCbGvqvfpeBDHXTWdH9R8aNUFzzzpOaS+lAngVFD5RWvCDQKBgChwEE4zQBQ3PWcmv7whKaZv1A6K0MMhGfNrpIoi33gkXSo+xyCyP6wD+kvqGpPW/XmHP8r1qOdlhcMYkORaCIyTe+c/7qqIXx1m3tsP/WCDVkySP+zI51EzOZ+AjjK5uexhWa8sQ3qB/tZoYz2yncejIumlAJslC/M/GiHZcVNt";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private int id;
    private int notify;
    private String price;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        showAlert(AlipayOfSandbox.this, getString(R.string.pay_success) + payResult);
                        Toast.makeText(getApplicationContext(),"Payment success",Toast.LENGTH_SHORT).show();
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
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showAlert(AlipayOfSandbox.this, getString(R.string.pay_failed) + payResult);
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        showAlert(AlipayOfSandbox.this, getString(R.string.auth_success) + authResult);
                    } else {
                        // 其他状态值则为授权失败
                        showAlert(AlipayOfSandbox.this, getString(R.string.auth_failed) + authResult);
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

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
                    OkHttpClient client = new OkHttpClient(); //创建http客户端
                    // 创建 JSON 格式的请求体
                    JSONObject json = new JSONObject();
                    json.put("username", username);


                    RequestBody body = RequestBody.create(
                            json.toString(), // 将 JSON 对象转换为字符串
                            MediaType.parse("application/json; charset=utf-8") // 指定请求体类型为 JSON
                    );

                    Request request = new Request.Builder()
                            .url(url+"/user/getOne")    //需要本机IP地址
                            .post(body)
                            .build();//创造http请求
                    Response response = client.newCall(request).execute();//执行发送指令
                    String a = response.body().string();
                    JSONObject jsonObject = new JSONObject(a);
                    System.out.println(jsonObject);
                    if (jsonObject.getString("code").equals("200")) {
                        // 获取 data 数组
                        JSONArray dataArray = jsonObject.getJSONArray("data");

                        // 从数组中获取第一个元素
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
        // 设置“确认交易”按钮的点击事件
        confirmTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePayment(); // 调用处理支付方法
            }
        });

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
    private void updateRadioButtons() {
        for (int i = 0; i < paymentMethodsContainer.getChildCount(); i++) {
            View child = paymentMethodsContainer.getChildAt(i);
            RadioButton radioButton = child.findViewById(R.id.paymentRadioButton);
            radioButton.setChecked(paymentMethods.get(i).getId() == selectedPaymentMethodId);
        }
    }

    private void handlePayment() {
        if (selectedPaymentMethodId == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return; // 没有选择支付方式时返回
        }

        // 获取对应的支付方式名称
        String selectedPaymentMethodName = null;
        for (PaymentMethod method : paymentMethods) {
            if (method.getId() == selectedPaymentMethodId) {
                selectedPaymentMethodName = method.getName();
                break; // 找到后退出循环
            }
        }

        if (selectedPaymentMethodName != null) {
            // 继续处理支付逻辑
            // 可以在这里使用 selectedPaymentMethodName
            Toast.makeText(this, "The payment method chosen: " + selectedPaymentMethodName, Toast.LENGTH_SHORT).show();
            if (selectedPaymentMethodName.equals("Alipay")){
                payV2();
            }else {
                String finalPaymentMethod = selectedPaymentMethodName;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        String formattedDateTime = now.format(formatter);

                        // 创建 JSON 格式的请求体
                        JSONObject json = new JSONObject();
                        try {
                            json.put("id", id);
                            json.put("status", 1);
                            json.put("paymentTime", formattedDateTime);
                            json.put("paymentMethod", finalPaymentMethod);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return; // 发生错误时返回
                        }

                        String info = "用" + finalPaymentMethod + "支付学费单成功！";
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
                                // 创建通知请求
                                JSONObject json1 = new JSONObject();
                                try {
                                    json1.put("id", id);
                                    json1.put("content", info);
                                    json1.put("uid", userid);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    return; // 发生错误时返回
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
                            e.printStackTrace(); // 捕获异常
                        }
                    }
                }).start();

            }
        } else {
            Toast.makeText(this, "No corresponding payment method found", Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * 支付宝支付业务示例
     */
    public void payV2() {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            showAlert(this, getString(R.string.error_missing_appid_rsa_private));
            return;
        }

        /*
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo 的获取必须来自服务端；
         */
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

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝账户授权业务示例
     */
    public void authV2(View v) {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
                || TextUtils.isEmpty(TARGET_ID)) {
            showAlert(this, getString(R.string.error_auth_missing_partner_appid_rsa_private_target_id));
            return;
        }

        /*
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo 的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        final String authInfo = info + "&" + sign;
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(AlipayOfSandbox.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    /**
     * 获取支付宝 SDK 版本号。
     */
    public void showSdkVersion(View v) {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        showAlert(this, getString(R.string.alipay_sdk_version_is) + version);
    }

    /**
     * 将 H5 网页版支付转换成支付宝 App 支付的示例
     */
    public void h5Pay(View v) {
        WebView.setWebContentsDebuggingEnabled(true);
        Intent intent = new Intent(this, AlipayOfSandbox.class);
        Bundle extras = new Bundle();

        /*
         * URL 是要测试的网站，在 Demo App 中会使用 H5PayDemoActivity 内的 WebView 打开。
         *
         * 可以填写任一支持支付宝支付的网站（如淘宝或一号店），在网站中下订单并唤起支付宝；
         * 或者直接填写由支付宝文档提供的“网站 Demo”生成的订单地址
         * （如 https://mclient.alipay.com/h5Continue.htm?h5_route_token=303ff0894cd4dccf591b089761dexxxx）
         * 进行测试。
         *
         * H5PayDemoActivity 中的 MyWebViewClient.shouldOverrideUrlLoading() 实现了拦截 URL 唤起支付宝，
         * 可以参考它实现自定义的 URL 拦截逻辑。
         *
         * 注意：WebView 的 shouldOverrideUrlLoading(url) 无法拦截直接调用 open(url) 打开的第一个 url，
         * 所以直接设置 url = "https://mclient.alipay.com/cashier/mobilepay.htm......" 是无法完成网页转 Native 的。
         * 如果需要拦截直接打开的支付宝网页支付 URL，可改为使用 shouldInterceptRequest(view, request) 。
         */
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
