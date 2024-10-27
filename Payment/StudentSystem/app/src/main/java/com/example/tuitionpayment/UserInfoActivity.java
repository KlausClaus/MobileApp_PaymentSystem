package com.example.tuitionpayment;

import static com.example.tuitionpayment.util.GlobalUrl.url;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UserInfoActivity extends AppCompatActivity {
    private String currentUsername;
    private EditText editTextNickname;
    private EditText editTextPassword;
    private TextView textViewUsername;
    SharedPreferences usInfo;
    private String userid;
    private String nickname;
    private String username;
    private String password;
    private String backurl;
    private String avatar1;

    Button button_upload_avatar;
    ImageView image_view_avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        usInfo = getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        userid = usInfo.getString("userid", null);
        username = usInfo.getString("username", null);
        password = usInfo.getString("password", null);
        avatar1 = usInfo.getString("avatar", null);
        URL url = null;
        try {
            url = new URL(avatar1);
            requestImg(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        textViewUsername = findViewById(R.id.text_view_username_value);
        editTextPassword = findViewById(R.id.edit_text_password);
        button_upload_avatar = findViewById(R.id.button_upload_avatar);
        image_view_avatar = findViewById(R.id.image_view_avatar);
        Button buttonSave = findViewById(R.id.button_save);
        textViewUsername.setText(username);
        editTextPassword.setText(password);
        button_upload_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        });

        buttonSave.setOnClickListener(v -> saveChanges());
    }


    private void saveChanges() {
        String newPassword = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(UserInfoActivity.this, "No modified content", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient(); //创建http客户端
                    if (backurl!=null){
                        File file = new File(backurl);
                        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);//通过表单上传文件
                        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"),file);//上传文件及其类型
                        requestBody.addFormDataPart("file",file.getName(),fileBody);//参数：请求的key，文件名称，fileBody
                        Request request = new Request.Builder()
                                .url(url+"/files/file")
                                .post(requestBody.build())
                                .build();//http请求
                        client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                System.out.println("失败");
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                if (response.isSuccessful()){
                                    try {
                                        backurl = response.body().string();
                                        String password = String.valueOf(editTextPassword.getText());

                                        String json = "{\n" +
                                                "    \"uid\":" + "\"" + userid + "\",\n" +
                                                "    \"username\":" + "\"" + username + "\",\n" +
                                                "    \"password\":" + "\"" + password + "\",\n" +
                                                "    \"avatar\":" + "\"" + backurl + "\"\n" +
                                                "}";
                                        OkHttpClient client1 = new OkHttpClient(); //创建http客户端
                                        Request request1 = new Request.Builder()
                                                .url(url+"/user")    //需要本机IP地址
                                                .post(RequestBody.create(MediaType.parse("application/json"), json))
                                                .build();//创造http请求
                                        Response response1 = client1.newCall(request1).execute();//执行发送指令
                                        String ab = response1.body().string();
                                        JSONObject jsonObject = new JSONObject(ab);
                                        System.out.println(jsonObject);

                                        if (jsonObject.getString("code").equals("200")) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(UserInfoActivity.this, "Modified successfully！", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(UserInfoActivity.this,MainActivity.class);
                                                    startActivity(intent);
                                                    SharedPreferences.Editor editor = usInfo.edit();
                                                    try {
                                                        editor.putString("userid", userid);
                                                        editor.putString("username", username);
                                                        editor.putString("password", password);
                                                        editor.putString("avatar", backurl);
                                                        editor.apply();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }else {
                                    Log.d("File upload",response.message()+"error:body"+response.body().string());
                                }
                            }
                        });
                        Response response = client.newCall(request).execute();//执行发送指令
                        String a = response.body().string();
                        backurl = a;


                    }else {
                        try {
                            String password = String.valueOf(editTextPassword.getText());

                            String json = "{\n" +
                                    "    \"userid\":" + "\"" + userid + "\",\n" +
                                    "    \"username\":" + "\"" + username + "\",\n" +
                                    "    \"password\":" + "\"" + password + "\",\n" +
                                   "    \"avatar\":" + "\"" + avatar1 + "\"\n" +
                                    "}";
                            OkHttpClient client1 = new OkHttpClient(); //创建http客户端
                            Request request1 = new Request.Builder()
                                    .url(url+"/user")    //需要本机IP地址
                                    .post(RequestBody.create(MediaType.parse("application/json"), json))
                                    .build();//创造http请求
                            Response response1 = client1.newCall(request1).execute();//执行发送指令
                            String ab = response1.body().string();
                            JSONObject jsonObject = new JSONObject(ab);
                            System.out.println(jsonObject);

                            if (jsonObject.getString("code").equals("200")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UserInfoActivity.this, "Modified successfully！", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(UserInfoActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        SharedPreferences.Editor editor = usInfo.edit();
                                        try {
                                            editor.putString("userid", userid);
                                            editor.putString("username", username);
                                            editor.putString("password", password);
                                            editor.putString("user_nickname", nickname);
                                            editor.putString("avatar", backurl);
                                            editor.apply();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //从相册获得图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                String picSavePath = getImagePath(uri,null);
                backurl = picSavePath;

                image_view_avatar.setImageURI(Uri.parse(backurl));
            }
        }
    }

    //获取到图片的真实路径
    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }

        return path;
    }

    private void requestImg(final URL imgUrl)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(imgUrl.openStream());

                    showImg(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void showImg(final Bitmap bitmap){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                image_view_avatar.setImageBitmap(bitmap);
            }
        });
    }
}