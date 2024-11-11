package com.example.tuitionpayment.fraction;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tuitionpayment.PaidFeesActivity;
import com.example.tuitionpayment.R;
import com.example.tuitionpayment.UnpaidFeesActivity;

public class BillFragment extends Fragment {
    private View unpaidFeesView;
    private View paidFeesView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View root = inflater.inflate(R.layout.fragment_bill, container, false);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        unpaidFeesView = getActivity().findViewById(R.id.unpaid_fees);
        paidFeesView = getActivity().findViewById(R.id.paid_fees);

        // 设置点击未支付学费单的跳转
        unpaidFeesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UnpaidFeesActivity.class);
                startActivity(intent);
            }
        });

        // 设置点击已支付学费单的跳转
        paidFeesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PaidFeesActivity.class);
                startActivity(intent);
            }
        });
    }
}
