package com.example.tuitionpayment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tuitionpayment.R;
import com.example.tuitionpayment.RegisterActivity;
import com.example.tuitionpayment.util.GlobalUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    Button ivLogin;
    Button ivRegister;
    EditText username;
    EditText password;
    SharedPreferences usInfo;
    String url= GlobalUrl.url;
    String roles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    //初始化视图控件
    private void initViews() {
        ivLogin = findViewById(R.id.iv_login);
        ivRegister = findViewById(R.id.iv_register1);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.password);

        usInfo = this.getSharedPreferences("userinfo",this.MODE_PRIVATE);
        ivLogin.setOnClickListener(clickListener);
        ivRegister.setOnClickListener(clickListener);



    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.iv_login) {
                String userName = username.getText().toString();
                String passWord = password.getText().toString();  //获取用户输入的用户名和密码
                int role = 2;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            OkHttpClient client = new OkHttpClient(); //创建http客户端
                            // 创建 JSON 格式的请求体
                            JSONObject json = new JSONObject();
                            json.put("username", userName);
                            json.put("password", passWord);
                            json.put("role", role);

                            RequestBody body = RequestBody.create(
                                    json.toString(), // 将 JSON 对象转换为字符串
                                    MediaType.parse("application/json; charset=utf-8") // 指定请求体类型为 JSON
                            );

                            Request request = new Request.Builder()
                                    .url(url+"/user/login")    //需要本机IP地址
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

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent();
                                        if (role == 2) {
                                            intent.setClass(LoginActivity.this, MainActivity.class);
                                        }

                                        SharedPreferences.Editor editor = usInfo.edit();
                                        try {
                                            // 存储 UID, username 和 password
                                            editor.putString("userid", jsonObject1.getString("uid"));
                                            editor.putString("username", jsonObject1.getString("username"));
                                            editor.putString("password", passWord);

                                            // 存储 nickname (如果存在)
                                            if (jsonObject1.has("nickname") && !jsonObject1.getString("nickname").isEmpty()) {
                                                editor.putString("nickname", jsonObject1.getString("nickname"));
                                            }

                                            editor.apply();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        startActivity(intent);
                                        Toast.makeText(LoginActivity.this, "Login successful！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (jsonObject.getString("code").equals("600")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "The user name or password is incorrect！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this,"The user name or password is incorrect！",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                }).start();


            }else if (i == R.id.iv_register1){
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);          //跳转到注册页面
                startActivity(intent);
                Toast.makeText(LoginActivity.this,"Go to register！",Toast.LENGTH_SHORT).show();

            }
        }
    };
}