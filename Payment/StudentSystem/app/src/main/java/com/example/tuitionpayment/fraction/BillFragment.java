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

/**
 * Fragment representing the Bill section of the application.
 * Provides options to view unpaid and paid fee details.
 */
public class BillFragment extends Fragment {
    /**
     * View representing the unpaid fees section.
     */
    private View unpaidFeesView;

    /**
     * View representing the paid fees section.
     */
    private View paidFeesView;

    /**
     * Inflates the fragment layout.
     *
     * @param inflater  The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstance State of the fragment saved during configuration changes.
     * @return The root view of the fragment.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View root = inflater.inflate(R.layout.fragment_bill, container, false);

        return root;
    }

    /**
     * Called when the fragment's activity has been created and the fragment's view hierarchy is instantiated.
     * Sets up click listeners for unpaid and paid fees views to navigate to respective activities.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Initialize views for unpaid and paid fees
        unpaidFeesView = getActivity().findViewById(R.id.unpaid_fees);
        paidFeesView = getActivity().findViewById(R.id.paid_fees);

        // Set click listener to navigate to the Unpaid Fees Activity
        unpaidFeesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UnpaidFeesActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener to navigate to the Paid Fees Activity
        paidFeesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PaidFeesActivity.class);
                startActivity(intent);
            }
        });
    }
}
