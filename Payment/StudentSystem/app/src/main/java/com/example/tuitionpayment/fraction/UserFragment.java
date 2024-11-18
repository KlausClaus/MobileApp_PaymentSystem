package com.example.tuitionpayment.fraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tuitionpayment.LoginActivity;
import com.example.tuitionpayment.ManagePaymentActivity;
import com.example.tuitionpayment.NotifyActivity;
import com.example.tuitionpayment.R;
import com.example.tuitionpayment.UserInfoActivity;

/**
 * The UserFragment class represents a fragment that displays user-related options,
 * including managing payments, viewing user information, notifications, and logout functionality.
 */
public class UserFragment extends Fragment {

    private View paymentManager;
    private View userInfoView;
    private View my_notifications;
    private View iv_logout;

    SharedPreferences usInfo;
    private String userid;
    private String nickname;
    private String username;
    private String password;

    /**
     * Inflates the layout for this fragment.
     *
     * @param inflater           the LayoutInflater object used to inflate views in the fragment
     * @param container          the parent ViewGroup into which the fragment's view will be inserted
     * @param savedInstanceState the Bundle containing the fragment's previously saved state, if any
     * @return the root view of the fragment
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        return root;
    }

    /**
     * Called when the fragment's activity has been created and the fragment's view hierarchy is instantiated.
     * This method initializes shared preferences and sets up click listeners for the fragment's views.
     *
     * @param savedInstanceState the Bundle containing the fragment's previously saved state, if any
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        usInfo = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        userid = usInfo.getString("uid", null);
        username = usInfo.getString("username", null);
        password = usInfo.getString("password", null);
        nickname = usInfo.getString("nickname", null);



        paymentManager = getActivity().findViewById(R.id.manage_payment);
        userInfoView = getActivity().findViewById(R.id.user_info);
        my_notifications = getActivity().findViewById(R.id.my_notifications);
        iv_logout = getActivity().findViewById(R.id.logout);

        // Set up the click listener for the payment manager view
        paymentManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ManagePaymentActivity.class);
                startActivity(intent);
            }
        });
        // Set up the click listener for the notifications view
        userInfoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(intent);
            }
        });
        my_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NotifyActivity.class);
                startActivity(intent);
            }
        });


        // Set up the click listener for the logout view
        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = usInfo.edit();
                editor.clear();
                editor.apply();
                getActivity().getSharedPreferences("useinfo", 0).edit().clear().commit();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
