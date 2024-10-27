package com.example.tuitionpayment;

import static com.example.tuitionpayment.util.GlobalUrl.url;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tuitionpayment.Adapter.ItemAdapter;
import com.example.tuitionpayment.R;
import com.example.tuitionpayment.entity.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UnpaidFeesActivity extends AppCompatActivity {

    private ItemAdapter itemAdapter;
    private String currentUsername;
    private String currentUserID;
    private ListView itemListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpaid_fees);

        itemListView = findViewById(R.id.itemListview);

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        currentUsername = sharedPreferences.getString("username", "");
        currentUserID = sharedPreferences.getString("userid", "");

        loadItems();

    }

    private void loadItems() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient(); //创建http客户端
                    // 创建 JSON 格式的请求体
                    JSONObject json = new JSONObject();
                    json.put("studentEmail", currentUsername);
                    json.put("status", 0);

                    RequestBody body = RequestBody.create(
                            json.toString(), // 将 JSON 对象转换为字符串
                            MediaType.parse("application/json; charset=utf-8") // 指定请求体类型为 JSON
                    );

                    Request request = new Request.Builder()
                            .url(url+"/tuitionInvoice/listByEmailStatus")
                            .post(body)
                            .build();//创造http请求
                    Response response = client.newCall(request).execute();//执行发送指令
                    String a = response.body().string();
                    JSONObject jsonObject = new JSONObject(a);
                    System.out.println(jsonObject);
                    if (jsonObject.getString("code").equals("200")) {
                        // 获取 data 数组
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        List<Item> items = convertJsonArrayToList(dataArray);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (items != null && !items.isEmpty()) {
                                    itemAdapter = new ItemAdapter(UnpaidFeesActivity.this, items);
                                    itemListView.setAdapter(itemAdapter);
                                    //设置点击事件
                                    itemListView.setOnItemClickListener((parent, view, position, id) -> {
                                        Item selectedItem = items.get(position);
                                        Intent intent = new Intent(UnpaidFeesActivity.this, DetailActivity.class);
                                        intent.putExtra("item", selectedItem);
                                        startActivity(intent);
                                    });
                                } else {
                                    Toast.makeText(UnpaidFeesActivity.this, "No items found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private List<Item> convertJsonArrayToList(JSONArray jsonArray) throws JSONException {
        List<Item> itemList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Item item = new Item();  // 创建一个空的 Item 对象

            // 逐步设置每个属性
            item.setId(jsonObject.getInt("id"));
            item.setStudentName(jsonObject.getString("studentName"));
            item.setStudentEmail(jsonObject.getString("studentEmail"));
            item.setMajor(jsonObject.getString("major"));
            item.setAcademicYear(jsonObject.getString("academicYear"));
            item.setTotalFee(jsonObject.getDouble("totalFee"));
            item.setTuitionFee(jsonObject.getDouble("tuitionFee"));

            // 使用 optDouble 来处理可能为空的字段
            item.setAccommodationFee(jsonObject.optDouble("accommodationFee", 0.0));
            item.setBookFee(jsonObject.optDouble("bookFee", 0.0));
            item.setMaterialFee(jsonObject.optDouble("materialFee", 0.0));
            item.setActivityFee(jsonObject.optDouble("activityFee", 0.0));
            item.setExamFee(jsonObject.optDouble("examFee", 0.0));

            item.setPaymentMethod(jsonObject.getString("paymentMethod"));
//            item.setPaymentAmount(jsonObject.getDouble("paymentAmount"));
            item.setCreatedTime(jsonObject.getString("createdTime"));

            // 使用 optString 来处理可能为空的字符串字段
            item.setPaymentTime(jsonObject.optString("paymentTime", null));

            item.setStatus(jsonObject.getInt("status"));

            itemList.add(item);
        }

        return itemList;
    }

}