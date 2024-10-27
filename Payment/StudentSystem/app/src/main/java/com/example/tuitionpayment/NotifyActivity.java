package com.example.tuitionpayment;

import static com.example.tuitionpayment.util.GlobalUrl.url;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tuitionpayment.Adapter.NotificationAdapter;
import com.example.tuitionpayment.entity.Notification;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotifyActivity extends AppCompatActivity {

    private ListView notificationListView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;
    private String currentUsername;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        currentUsername = sharedPreferences.getString("username", "");
        currentUserID = sharedPreferences.getString("userid", "");

        notificationListView = findViewById(R.id.notification_list);
        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(this, notificationList);
        notificationListView.setAdapter(notificationAdapter);

        // 根据 uid 获取通知数据
        String uid = "your_uid_here"; // 你需要根据实际情况获取 uid
        fetchNotifications(uid);
    }

    private void fetchNotifications(String uid) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                // 创建 JSON 格式的请求体
                JSONObject json = new JSONObject();
                json.put("uid", currentUserID);

                RequestBody body = RequestBody.create(
                        json.toString(), // 将 JSON 对象转换为字符串
                        MediaType.parse("application/json; charset=utf-8") // 指定请求体类型为 JSON
                );

                Request request = new Request.Builder()
                        .url(url+"/notify/listByUid")    //需要本机IP地址
                        .post(body)
                        .build();//创造http请求
                Response response = client.newCall(request).execute();
                String a = response.body().string();
                JSONObject jsonObject1 = new JSONObject(a);

                JSONArray dataArray = jsonObject1   .getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject jsonObject = dataArray.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String content = jsonObject.getString("content");
                    String time = jsonObject.getString("time");

                    Notification notification = new Notification(id, content, time, uid);
                    notificationList.add(notification);
                }

                runOnUiThread(() -> notificationAdapter.notifyDataSetChanged());

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(NotifyActivity.this, "Failed to get notification", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
