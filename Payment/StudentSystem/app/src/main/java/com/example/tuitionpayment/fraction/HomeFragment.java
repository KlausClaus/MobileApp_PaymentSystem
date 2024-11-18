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

/**
 * Fragment representing the Home section of the application.
 * Displays a list of tuition items and allows toggling notifications.
 */
public class HomeFragment extends Fragment {

    /**
     * Adapter for displaying items in the ListView.
     */
    private ItemAdapter itemAdapter;

    /**
     * Current username of the logged-in user.
     */
    private String currentUsername;

    /**
     * Current user ID of the logged-in user.
     */
    private String currentUserID;

    /**
     * ListView to display the items.
     */
    private ListView itemListView;

    /**
     * Flag indicating whether notifications are enabled.
     */
    private boolean notificationsEnabled = true;

    /**
     * Inflates the fragment layout and initializes basic variables.
     *
     * @param inflater           The LayoutInflater object to inflate the views.
     * @param container          The parent view that the fragment's UI will be attached to.
     * @param savedInstanceState Previous state, if available.
     * @return The root view of the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        itemListView = root.findViewById(R.id.itemListview);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", getActivity().MODE_PRIVATE);
        currentUsername = sharedPreferences.getString("username", "");
        currentUserID = sharedPreferences.getString("userid", "");

        return root;
    }

    /**
     * Called after the fragment's activity is created.
     * Sets up notification toggle functionality and item loading.
     *
     * @param savedInstanceState The saved state of the fragment.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final int[] notify = {-1};
        ImageView notificationButton = getActivity().findViewById(R.id.notificationButton);

        // Initialize notification button based on user settings
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    JSONObject json = new JSONObject();
                    json.put("username", currentUsername);


                    RequestBody body = RequestBody.create(
                            json.toString(),
                            MediaType.parse("application/json; charset=utf-8")
                    );

                    Request request = new Request.Builder()
                            .url(url+"/user/getOne")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String a = response.body().string();
                    JSONObject jsonObject = new JSONObject(a);
                    System.out.println(jsonObject);
                    if (jsonObject.getString("code").equals("200")) {

                        JSONArray dataArray = jsonObject.getJSONArray("data");

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

        // Load items into the ListView
        loadItems();

        // Handle notification button toggle
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationsEnabled = !notificationsEnabled;
                if (notificationsEnabled) {

                    notify[0]=0;
                    notificationButton.setBackgroundResource(R.drawable.notify);

                } else {

                    notify[0]=1;
                    notificationButton.setBackgroundResource(R.drawable.no_notify);

                }
                // Update notification settings on the server
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();

                            JSONObject json = new JSONObject();
                            json.put("notify", notify[0]);
                            json.put("uid", currentUserID);


                            RequestBody body = RequestBody.create(
                                    json.toString(),
                                    MediaType.parse("application/json; charset=utf-8")
                            );

                            Request request = new Request.Builder()
                                    .url(url+"/user")
                                    .post(body)
                                    .build();//创造http请求
                            Response response = client.newCall(request).execute();
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


    /**
     * Loads the tuition items for the current user from the server.
     * Populates the ListView with the retrieved items.
     */
    private void loadItems() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    JSONObject json = new JSONObject();
                    json.put("studentEmail", currentUsername);

                    RequestBody body = RequestBody.create(
                            json.toString(),
                            MediaType.parse("application/json; charset=utf-8")
                    );

                    Request request = new Request.Builder()
                            .url(url+"/tuitionInvoice/listByEmail")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String a = response.body().string();
                    JSONObject jsonObject = new JSONObject(a);
                    System.out.println(jsonObject);
                    if (jsonObject.getString("code").equals("200")) {

                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        List<Item> items = convertJsonArrayToList(dataArray);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (items != null && !items.isEmpty()) {
                                    itemAdapter = new ItemAdapter(getActivity(), items);
                                    itemListView.setAdapter(itemAdapter);

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

    /**
     * Converts a JSON array to a list of {@link Item} objects.
     *
     * @param jsonArray The JSON array containing item data.
     * @return A list of items.
     * @throws JSONException If parsing the JSON array fails.
     */
    private List<Item> convertJsonArrayToList(JSONArray jsonArray) throws JSONException {
        List<Item> itemList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Item item = new Item();

            item.setId(jsonObject.getInt("id"));
            item.setStudentName(jsonObject.getString("studentName"));
            item.setStudentEmail(jsonObject.getString("studentEmail"));
            item.setMajor(jsonObject.getString("major"));
            item.setAcademicYear(jsonObject.getString("academicYear"));
            item.setTotalFee(jsonObject.getDouble("totalFee"));
            item.setTuitionFee(jsonObject.getDouble("tuitionFee"));

            item.setAccommodationFee(jsonObject.optDouble("accommodationFee", 0.0));
            item.setBookFee(jsonObject.optDouble("bookFee", 0.0));
            item.setMaterialFee(jsonObject.optDouble("materialFee", 0.0));
            item.setActivityFee(jsonObject.optDouble("activityFee", 0.0));
            item.setExamFee(jsonObject.optDouble("examFee", 0.0));

            item.setPaymentMethod(jsonObject.getString("paymentMethod"));
//            item.setPaymentAmount(jsonObject.getDouble("paymentAmount"));
            item.setCreatedTime(jsonObject.getString("createdTime"));

            item.setPaymentTime(jsonObject.optString("paymentTime", null));

            item.setStatus(jsonObject.getInt("status"));

            itemList.add(item);
        }

        return itemList;
    }


}