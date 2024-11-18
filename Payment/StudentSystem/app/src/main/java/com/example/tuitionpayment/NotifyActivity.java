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

/**
 * Activity for displaying user notifications fetched from the server.
 */
public class NotifyActivity extends AppCompatActivity {

    private ListView notificationListView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;
    private String currentUsername;
    private String currentUserID;

    /**
     * Called when the activity is starting. Initializes the notification list view
     * and fetches notifications for the current user.
     *
     * @param savedInstanceState the previously saved state of the activity, if any
     */
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

        String uid = "your_uid_here";
        // Fetch notifications for the current user
        fetchNotifications(uid);
    }

    /**
     * Fetches the notifications for the given user ID by sending a request to the server.
     *
     * @param uid the user ID for which to fetch notifications
     */
    private void fetchNotifications(String uid) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                // Create JSON request body
                JSONObject json = new JSONObject();
                json.put("uid", currentUserID);

                RequestBody body = RequestBody.create(
                        json.toString(), // Convert JSON object to string
                        MediaType.parse("application/json; charset=utf-8") // Specify request body type as JSON
                );

                Request request = new Request.Builder()
                        .url(url+"/notify/listByUid") // Server endpoint for fetching notifications
                        .post(body)
                        .build();// Build the HTTP request
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

                // Update the UI on the main thread
                runOnUiThread(() -> notificationAdapter.notifyDataSetChanged());

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(NotifyActivity.this, "Failed to get notification", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
