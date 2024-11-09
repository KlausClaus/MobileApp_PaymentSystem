package com.example.tuitionpayment.fraction;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tuitionpayment.ManagePaymentActivity;
import com.example.tuitionpayment.R;

public class PaymentFragment extends Fragment {

    private LinearLayout managePaymentView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View root = inflater.inflate(R.layout.fragment_payment, container, false);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        managePaymentView = getActivity().findViewById(R.id.manage_payment);

        // 设置点击管理支付方式的跳转
        managePaymentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ManagePaymentActivity.class);
                startActivity(intent);
            }
        });

    }
}
