package com.example.tuitionpayment.fraction;

import static com.example.tuitionpayment.util.GlobalUrl.url;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.tuitionpayment.Adapter.ItemAdapter;
import com.example.tuitionpayment.DetailActivity;
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

public class HomeFragment extends Fragment {

    private ItemAdapter itemAdapter;
    private String currentUsername;
    private String currentUserID;
    private ListView itemListView;
    private boolean notificationsEnabled = true; // 默认开启通知

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        itemListView = root.findViewById(R.id.itemListview);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", getActivity().MODE_PRIVATE);
        currentUsername = sharedPreferences.getString("username", "");
        currentUserID = sharedPreferences.getString("userid", "");

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final int[] notify = {-1};
        ImageView notificationButton = getActivity().findViewById(R.id.notificationButton);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient(); //创建http客户端
                    // 创建 JSON 格式的请求体
                    JSONObject json = new JSONObject();
                    json.put("username", currentUsername);


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
                        notify[0] = jsonObject1.getInt("notify");
                        System.out.println("111111122222");
                        System.out.println(notify[0]);
                        if (notify[0]==0){
                            notificationButton.setBackgroundResource(R.drawable.notify);
                            notificationsEnabled = true;
                        }else {
                            notificationsEnabled = false;
                            notificationButton.setBackgroundResource(R.drawable.no_notify);

                        }

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        loadItems();
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationsEnabled = !notificationsEnabled; // 切换状态
                if (notificationsEnabled) {
                    // 在这里实现开启通知的逻辑
                    notify[0]=0;
                    notificationButton.setBackgroundResource(R.drawable.notify);

                } else {
                    // 在这里实现关闭通知的逻辑
                    notify[0]=1;
                    notificationButton.setBackgroundResource(R.drawable.no_notify);

                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient(); //创建http客户端
                            // 创建 JSON 格式的请求体
                            JSONObject json = new JSONObject();
                            json.put("notify", notify[0]);
                            json.put("uid", currentUserID);


                            RequestBody body = RequestBody.create(
                                    json.toString(), // 将 JSON 对象转换为字符串
                                    MediaType.parse("application/json; charset=utf-8") // 指定请求体类型为 JSON
                            );

                            Request request = new Request.Builder()
                                    .url(url+"/user")    //需要本机IP地址
                                    .post(body)
                                    .build();//创造http请求
                            Response response = client.newCall(request).execute();//执行发送指令
                            String a = response.body().string();
                            JSONObject jsonObject = new JSONObject(a);
                            System.out.println(jsonObject);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
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

                    RequestBody body = RequestBody.create(
                            json.toString(), // 将 JSON 对象转换为字符串
                            MediaType.parse("application/json; charset=utf-8") // 指定请求体类型为 JSON
                    );

                    Request request = new Request.Builder()
                            .url(url+"/tuitionInvoice/listByEmail")
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (items != null && !items.isEmpty()) {
                                    itemAdapter = new ItemAdapter(getActivity(), items);
                                    itemListView.setAdapter(itemAdapter);
                                     //设置点击事件
                                    itemListView.setOnItemClickListener((parent, view, position, id) -> {
                                        Item selectedItem = items.get(position);
                                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                                        intent.putExtra("item", selectedItem);
                                        startActivity(intent);
                                    });
                                } else {
                                    Toast.makeText(getContext(), "No items found", Toast.LENGTH_SHORT).show();
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
            item.setPaymentAmount(jsonObject.getDouble("paymentAmount"));
            item.setCreatedTime(jsonObject.getString("createdTime"));

            // 使用 optString 来处理可能为空的字符串字段
            item.setPaymentTime(jsonObject.optString("paymentTime", null));

            item.setStatus(jsonObject.getInt("status"));

            itemList.add(item);
        }

        return itemList;
    }


}