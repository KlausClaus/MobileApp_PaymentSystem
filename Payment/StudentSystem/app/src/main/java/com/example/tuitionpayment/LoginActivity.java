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
import com.example.tuitionpayment.util.Md5Utils;

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

/**
 * The LoginActivity class handles user login functionality, including username and password input,
 * password encryption using MD5, and user authentication via a server API.
 */
public class LoginActivity extends AppCompatActivity {

    Button ivLogin;
    Button ivRegister;
    EditText username;
    EditText password;
    SharedPreferences usInfo;
    String url= GlobalUrl.url;
    String roles;

    /**
     * Called when the activity is starting. This method initializes the activity's views and
     * components.
     *
     * @param savedInstanceState the previously saved state of the activity, if any
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    /**
     * Initializes the views and components of the activity, including setting up button click
     * listeners.
     */
    private void initViews() {
        ivLogin = findViewById(R.id.iv_login);
        ivRegister = findViewById(R.id.iv_register1);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.password);

        usInfo = this.getSharedPreferences("userinfo",this.MODE_PRIVATE);
        ivLogin.setOnClickListener(clickListener);
        ivRegister.setOnClickListener(clickListener);



    }

    /**
     * Handles click events for the login and register buttons.
     */
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.iv_login) {
                String userName = username.getText().toString();
                String passWord = password.getText().toString();  // Retrieve user-entered username and password
                int role = 2;

                String passWordMd5 = Md5Utils.encryptMD5(passWord);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient(); // Create HTTP client
                            // 创建 JSON 格式的请求体
                            JSONObject json = new JSONObject();
                            json.put("username", userName);
                            json.put("password", passWordMd5);
                            json.put("role", role);

                            RequestBody body = RequestBody.create(
                                    json.toString(), // 将 JSON 对象转换为字符串
                                    MediaType.parse("application/json; charset=utf-8") // Specify content type as JSON
                            );

                            Request request = new Request.Builder()
                                    .url(url+"/user/login")    // Set request URL
                                    .post(body)
                                    .build();//创造http请求
                            Response response = client.newCall(request).execute();// Execute request
                            String a = response.body().string();
                            JSONObject jsonObject = new JSONObject(a);
                            System.out.println(jsonObject);
                            if (jsonObject.getString("code").equals("200")) {
                                // Retrieve data array from response
                                JSONArray dataArray = jsonObject.getJSONArray("data");

                                // Get the first element from the array
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
                                            // Store user information in SharedPreferences
                                            editor.putString("userid", jsonObject1.getString("uid"));
                                            editor.putString("username", jsonObject1.getString("username"));
                                            editor.putString("password", passWord);

                                            // Store nickname if it exists
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
                intent.setClass(LoginActivity.this, RegisterActivity.class); // Navigate to the register page
                startActivity(intent);
                Toast.makeText(LoginActivity.this,"Go to register！",Toast.LENGTH_SHORT).show();

            }
        }
    };
}