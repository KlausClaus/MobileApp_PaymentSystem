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

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegisterActivity extends AppCompatActivity {

    EditText usename,usepwd;
    Button submit;
    SharedPreferences sp;
    String url= GlobalUrl.url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usename = this.findViewById(R.id.usName);			    //用户名编辑框
        usepwd =  this.findViewById(R.id.usPassword1);				//设置初始密码编辑框
        submit =   this.findViewById(R.id.iv_register2);				//注册按钮
        sp = this.getSharedPreferences("useinfo",this.MODE_PRIVATE);
        usename.setBackgroundResource(R.drawable.text_view_background);
        usepwd.setBackgroundResource(R.drawable.text_view_background);
        submit.setBackgroundResource(R.drawable.register_login_buttonbackground);


        submit.setOnClickListener(new View.OnClickListener() {
            boolean flag = true;            //判断用户是否已存在的标志位
            @Override
            public void onClick(View v) {
                String name = usename.getText().toString();                //用户名
                String pwd01 = usepwd.getText().toString();                //密码
                if(name.equals("")||pwd01 .equals("")){
                    Toast.makeText(RegisterActivity.this, "The user name or password cannot be empty!！", Toast.LENGTH_LONG).show();
                }
                else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String json = "{\n" +
                                        "    \"username\":"+"\""+name+"\",\n" +
                                        "    \"role\":"+"\""+2+"\",\n" +
                                        "    \"password\":"+"\""+pwd01+"\"\n" +
                                        "}";
                                OkHttpClient client = new OkHttpClient(); //创建http客户端
                                Request request = new Request.Builder()
                                        .url(url+"/user/register")    //需要本机IP地址
                                        .post(RequestBody.create(MediaType.parse("application/json"),json))
                                        .build();//创造http请求
                                Response response = client.newCall(request).execute();//执行发送指令
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
}
