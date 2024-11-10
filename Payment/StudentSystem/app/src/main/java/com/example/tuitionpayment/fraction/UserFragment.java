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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        return root;
    }

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


        paymentManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ManagePaymentActivity.class);
                startActivity(intent);
            }
        });
        // 设置点击个人信息的跳转
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


        // 设置点击退出登录的功能
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
