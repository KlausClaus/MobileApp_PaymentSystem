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

/**
 * Activity for displaying a list of unpaid tuition fees for the current user.
 */
public class UnpaidFeesActivity extends AppCompatActivity {

    private ItemAdapter itemAdapter;
    private String currentUsername;
    private String currentUserID;
    private ListView itemListView;

    /**
     * Called when the activity is starting. Initializes the UI and loads unpaid fee items.
     *
     * @param savedInstanceState the previously saved state of the activity, if any
     */
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

    /**
     * Fetches the list of unpaid fee items from the server and updates the UI.
     */
    private void loadItems() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    JSONObject json = new JSONObject();
                    json.put("studentEmail", currentUsername);
                    json.put("status", 0);

                    RequestBody body = RequestBody.create(
                            json.toString(),
                            MediaType.parse("application/json; charset=utf-8")
                    );

                    Request request = new Request.Builder()
                            .url(url+"/tuitionInvoice/listByEmailStatus")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String a = response.body().string();
                    JSONObject jsonObject = new JSONObject(a);
                    System.out.println(jsonObject);
                    if (jsonObject.getString("code").equals("200")) {

                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        List<Item> items = convertJsonArrayToList(dataArray);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (items != null && !items.isEmpty()) {
                                    itemAdapter = new ItemAdapter(UnpaidFeesActivity.this, items);
                                    itemListView.setAdapter(itemAdapter);

                                    // Set click listener for list items
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

    /**
     * Converts a JSON array of items to a list of {@link Item} objects.
     *
     * @param jsonArray the JSON array to be converted
     * @return a list of {@link Item} objects
     * @throws JSONException if a parsing error occurs
     */
    private List<Item> convertJsonArrayToList(JSONArray jsonArray) throws JSONException {
        List<Item> itemList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Item item = new Item();

            // Set attributes of the item
            item.setId(jsonObject.getInt("id"));
            item.setStudentName(jsonObject.getString("studentName"));
            item.setStudentEmail(jsonObject.getString("studentEmail"));
            item.setMajor(jsonObject.getString("major"));
            item.setAcademicYear(jsonObject.getString("academicYear"));
            item.setTotalFee(jsonObject.getDouble("totalFee"));
            item.setTuitionFee(jsonObject.getDouble("tuitionFee"));

            // Handle optional fields using optDouble
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