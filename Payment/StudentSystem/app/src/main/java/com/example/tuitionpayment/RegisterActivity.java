package com.example.tuitionpayment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tuitionpayment.util.GlobalUrl;
import com.example.tuitionpayment.util.Md5Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Arrays;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Activity for user registration. Handles user input, password encryption,
 * server-side registration, and addition of default payment methods.
 */
public class RegisterActivity extends AppCompatActivity {

    EditText usename,usepwd;
    Button submit;
    SharedPreferences sp;
    String url= GlobalUrl.url;

    /**
     * Called when the activity is starting. Initializes the UI and sets up
     * click listeners for the registration process.
     *
     * @param savedInstanceState the previously saved state of the activity, if any
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usename = this.findViewById(R.id.usName); // Username input field
        usepwd = this.findViewById(R.id.usPassword1); // Password input field
        submit = this.findViewById(R.id.iv_register2); // Register button
        sp = this.getSharedPreferences("useinfo",this.MODE_PRIVATE);
        usename.setBackgroundResource(R.drawable.text_view_background);
        usepwd.setBackgroundResource(R.drawable.text_view_background);
        submit.setBackgroundResource(R.drawable.register_login_buttonbackground);


        submit.setOnClickListener(new View.OnClickListener() {
            boolean flag = true;
            @Override
            public void onClick(View v) {
                String name = usename.getText().toString();
                String pwd01 = usepwd.getText().toString();
                if(name.equals("")||pwd01 .equals("")){
                    Toast.makeText(RegisterActivity.this, "The user name or password cannot be empty!！", Toast.LENGTH_LONG).show();
                }
                else {
                    String passWordMd5 = Md5Utils.encryptMD5(pwd01);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String json = "{\n" +
                                        "    \"username\":"+"\""+name+"\",\n" +
                                        "    \"role\":"+"\""+2+"\",\n" +
                                        "    \"notify\":"+"\""+0+"\",\n" +
                                        "    \"password\":"+"\""+passWordMd5+"\"\n" +
                                        "}";
                                OkHttpClient client = new OkHttpClient(); // Create HTTP client
                                Request request = new Request.Builder()
                                        .url(url+"/user/register")
                                        .post(RequestBody.create(MediaType.parse("application/json"),json))
                                        .build();// Build the HTTP request
                                Response response = client.newCall(request).execute();
                                String a = response.body().string();
                                JSONObject jsonObject = new JSONObject(a);
                                System.out.println(jsonObject);
                                if (jsonObject.getString("code").equals("200")){


                                    Intent intent = new Intent();
                                    intent.setClass(RegisterActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this,"Registered successfully！ Please log in！",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    addDefaultPaymentMethods(name);

                                }else if (jsonObject.getString("code").equals("600")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this,"Please enter the correct email address！",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this,"Network connection failure",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    }).start();
                }
            }

        });
    }

    /**
     * Adds default payment methods for the newly registered user.
     *
     * @param username the username of the new user
     */
    private void addDefaultPaymentMethods(String username) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                List<String> defaultPaymentMethods = Arrays.asList("UnionPay", "WeChat", "AliPay", "Visa", "ApplePay");
    
                for (String methodName : defaultPaymentMethods) {
                    JSONObject json = new JSONObject();
                    json.put("email", username);
                    json.put("payway", methodName);
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
                    if (!response.isSuccessful()) {
                        throw new Exception("Failed to add payment method: " + methodName);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, "Failed to add default payment methods", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
    
}
