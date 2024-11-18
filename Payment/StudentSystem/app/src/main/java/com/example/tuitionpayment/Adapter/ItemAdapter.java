package com.example.tuitionpayment.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.tuitionpayment.R;
import com.example.tuitionpayment.entity.Item;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


/**
 * Custom adapter for displaying a list of {@link Item} objects in a ListView or GridView.
 * Provides a reusable layout for each item in the list.
 */
public class ItemAdapter extends ArrayAdapter<Item> {

    /**
     * Context of the application or activity using this adapter.
     */
    private Context context;

    /**
     * Constructor for the {@link ItemAdapter}.
     *
     * @param context The context of the calling activity or application.
     * @param items   The list of {@link Item} objects to display.
     */
    public ItemAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        this.context = context;
    }

    /**
     * Provides a view for an adapter view (e.g., ListView) to display data at the specified position.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent view that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position); // Retrieve the current Item instance
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            // Inflate the custom layout for the item
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.academicYear = view.findViewById(R.id.academicYear);
            viewHolder.totalFee = view.findViewById(R.id.totalFee);
            viewHolder.major = view.findViewById(R.id.major);
            viewHolder.createdTime = view.findViewById(R.id.createdTime);
            view.setTag(viewHolder); // Store ViewHolder in the view
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // Reuse the existing ViewHolder
        }

        // Set data to the views
        viewHolder.academicYear.setText(item.getAcademicYear());
        viewHolder.createdTime.setText(item.getCreatedTime());
        viewHolder.major.setText(item.getMajor());
        viewHolder.totalFee.setText("$" + String.valueOf(item.getTotalFee()));

        return view;
    }

    /**
     * ViewHolder class to cache the views for better performance.
     * Holds references to the TextViews in the layout.
     */
    static class ViewHolder {
        TextView academicYear;
        TextView totalFee;
        TextView createdTime;
        TextView major;
    }
}