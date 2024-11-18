package com.example.tuitionpayment.fraction;

import static com.example.tuitionpayment.util.GlobalUrl.url;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.tuitionpayment.ManagePaymentActivity;
import com.example.tuitionpayment.R;

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
 * Fragment for managing payment methods.
 * Provides functionality for adding, deleting, and setting default payment methods.
 */
public class PaymentFragment extends Fragment {

    /**
     * Container for displaying payment methods.
     */
    private LinearLayout paymentMethodsContainer;

    /**
     * Button for adding a new payment method.
     */
    private Button addPaymentMethod;

    /**
     * Button for confirming and saving payment method settings.
     */
    private Button confirmTransaction;

    /**
     * SharedPreferences to store user information.
     */
    private SharedPreferences usInfo;

    /**
     * Username of the current user.
     */
    private String username;

    /**
     * List of available payment methods.
     */
    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    /**
     * ID of the currently selected payment method.
     */
    private int selectedPaymentMethodId = -1;

    /**
     * ID of the initial default payment method.
     */
    private int chushiId = -1;

    /**
     * Inflates the fragment layout.
     *
     * @param inflater           The LayoutInflater object to inflate the views.
     * @param container          The parent view that the fragment's UI will be attached to.
     * @param savedInstanceState Previous state, if available.
     * @return The root view of the fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_payment, container, false);
        return root;
    }


    /**
     * Called after the fragment's activity is created.
     * Sets up the UI elements and loads payment methods.
     *
     * @param savedInstanceState The saved state of the fragment.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        usInfo = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        username = usInfo.getString("username", null);

        paymentMethodsContainer = getActivity().findViewById(R.id.paymentMethodsContainer);
        addPaymentMethod = getActivity().findViewById(R.id.addPaymentMethod);
        confirmTransaction = getActivity().findViewById(R.id.confirmTransaction);

        loadPaymentMethods();

        addPaymentMethod.setOnClickListener(v -> showAddPaymentMethodDialog());
        confirmTransaction.setOnClickListener(v -> savePaymentMethods());
    }

    /**
     * Loads payment methods from the server and updates the UI.
     */
    private void loadPaymentMethods() {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                JSONObject json = new JSONObject();
                json.put("email", username);

                RequestBody body = RequestBody.create(
                        json.toString(),
                        MediaType.parse("application/json; charset=utf-8")
                );
                Request request = new Request.Builder()
                        .url(url + "/payway/listByEmail")
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                if (jsonObject.getString("code").equals("200")) {
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject paymentObject = dataArray.getJSONObject(i);
                        PaymentMethod paymentMethod = new PaymentMethod(
                                paymentObject.getInt("id"),
                                paymentObject.getString("payway"),
                                paymentObject.getInt("isDefault")
                        );
                        paymentMethods.add(paymentMethod);
                    }
                    getActivity().runOnUiThread(this::updatePaymentMethodsView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Updates the UI to display the current list of payment methods.
     */
    private void updatePaymentMethodsView() {
        paymentMethodsContainer.removeAllViews();
        for (PaymentMethod method : paymentMethods) {
            View paymentMethodView = LayoutInflater.from(getContext()).inflate(R.layout.item_payment_method, paymentMethodsContainer, false);
            ImageView icon = paymentMethodView.findViewById(R.id.paymentIcon);
            TextView name = paymentMethodView.findViewById(R.id.paymentName);
            RadioButton radioButton = paymentMethodView.findViewById(R.id.paymentRadioButton);
            Button deleteButton = paymentMethodView.findViewById(R.id.deletePaymentMethod);

            // Set icon based on payment method name
            // You need to add proper drawables for each payment method
            icon.setImageResource(getIconResourceIdForPaymentMethod(method.getName()));

            name.setText(method.getName());
            if (method.isDefault() == 1) {
                chushiId = method.getId();
                radioButton.setChecked(true);

            }

            radioButton.setOnClickListener(v -> {
                selectedPaymentMethodId = method.getId();
                updateRadioButtons();
            });

            deleteButton.setOnClickListener(v -> {
                // Show confirmation dialog before deleting
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete Payment Method");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    // Call the method to delete the payment method
                    deletePaymentMethod(method);
                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                builder.create().show();
            });

            paymentMethodsContainer.addView(paymentMethodView);
        }
    }

    /**
     * Gets the icon resource ID for a given payment method name.
     *
     * @param methodName The name of the payment method.
     * @return The resource ID of the corresponding icon.
     */
    private int getIconResourceIdForPaymentMethod(String methodName) {
        switch (methodName.toLowerCase()) {
            case "alipay":
                return R.drawable.alipay_logo;
            case "wechat":
                return R.drawable.wechat_logo;
            case "applepay":
                return R.drawable.applepay_logo;
            case "unionpay":
                return R.drawable.yinlian_logo;
            case "visa":
                return R.drawable.visa_logo;
            default:
                return 0;
        }
    }

    /**
     * Updates the radio buttons to reflect the currently selected payment method.
     */
    private void updateRadioButtons() {
        for (int i = 0; i < paymentMethodsContainer.getChildCount(); i++) {
            View child = paymentMethodsContainer.getChildAt(i);
            RadioButton radioButton = child.findViewById(R.id.paymentRadioButton);
            radioButton.setChecked(paymentMethods.get(i).getId() == selectedPaymentMethodId);
        }
    }

    /**
     * Displays a dialog to add a new payment method.
     */
    private void showAddPaymentMethodDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Payment Method");

        // 设置输入框
        final EditText input = new EditText(getContext());
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String newPaymentMethod = input.getText().toString().trim();
            if (!newPaymentMethod.isEmpty()) {
                addNewPaymentMethod(newPaymentMethod);
            } else {
                Toast.makeText(getContext(), "The payment method cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * Adds a new payment method to the server and updates the UI.
     *
     * @param paymentMethodName The name of the payment method to add.
     */
    private void addNewPaymentMethod(String paymentMethodName) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                JSONObject json = new JSONObject();
                json.put("email", username);
                json.put("payway", paymentMethodName);
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
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                if (jsonObject.getString("code").equals("200")) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    PaymentMethod newMethod = new PaymentMethod(
                            data.getInt("id"),
                            data.getString("payway"),
                            data.getInt("isDefault")
                    );
                    paymentMethods.add(newMethod);
                    getActivity().runOnUiThread(this::updatePaymentMethodsView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Deletes a specified payment method by making a network request.
     * The method runs in a background thread to avoid blocking the main thread.
     *
     * @param method the payment method to be deleted
     */
    private void deletePaymentMethod(PaymentMethod method) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url + "/payway/del/" + method.getId())
                        .delete()
                        .build();
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                if (jsonObject.getString("code").equals("200")) {
                    paymentMethods.remove(method);
                    getActivity().runOnUiThread(this::updatePaymentMethodsView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Saves the current payment method settings by making network requests to update the default payment method.
     * If the selected payment method is different from the original, the original isDefault flag is reset to 0,
     * and the selected payment method isDefault flag is set to 1.
     * The operation is performed in a background thread.
     */
    private void savePaymentMethods() {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                if (chushiId != selectedPaymentMethodId) {
                    // Update the original default payment method to isDefault = 0
                    JSONObject updateDefaultJson = new JSONObject();
                    updateDefaultJson.put("id", chushiId);
                    updateDefaultJson.put("isDefault", 0);

                    RequestBody updateDefaultBody = RequestBody.create(
                            updateDefaultJson.toString(),
                            MediaType.parse("application/json; charset=utf-8")
                    );
                    Request updateDefaultRequest = new Request.Builder()
                            .url(url + "/payway")
                            .post(updateDefaultBody)
                            .build();
                    client.newCall(updateDefaultRequest).execute(); // Execute the update request
                }

                // Set the selected payment method's isDefault to 1
                JSONObject setDefaultJson = new JSONObject();
                setDefaultJson.put("email", username);
                setDefaultJson.put("isDefault", 1);
                setDefaultJson.put("id", selectedPaymentMethodId);

                RequestBody setDefaultBody = RequestBody.create(
                        setDefaultJson.toString(),
                        MediaType.parse("application/json; charset=utf-8")
                );
                Request setDefaultRequest = new Request.Builder()
                        .url(url + "/payway")
                        .post(setDefaultBody)
                        .build();
                Response response = client.newCall(setDefaultRequest).execute();

                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                if (jsonObject.getString("code").equals("200")) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Settings saved", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getActivity(), ManagePaymentActivity.class);
//                        Intent intent = new Intent(getActivity(), PaymentFragment.class);
//                        startActivity(intent);
//                        getActivity().finish();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Represents a payment method with an ID, name, and default status.
     */
    private static class PaymentMethod {
        private int id;
        private String name;
        private int isDefault;

        /**
         * Constructs a PaymentMethod instance with the specified ID, name, and default status.
         *
         * @param id        the unique identifier for the payment method
         * @param name      the name of the payment method
         * @param isDefault the default status of the payment method (1 if default, 0 otherwise)
         */
        public PaymentMethod(int id, String name, int isDefault) {
            this.id = id;
            this.name = name;
            this.isDefault = isDefault;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int isDefault() {
            return isDefault;
        }
    }
}
