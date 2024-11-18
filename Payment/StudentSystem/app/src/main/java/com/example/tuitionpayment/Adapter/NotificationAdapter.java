package com.example.tuitionpayment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tuitionpayment.entity.Notification;

import java.util.List;


/**
 * Custom adapter for displaying a list of {@link Notification} objects in a ListView.
 * Each notification displays its time and content in a simple layout.
 */
public class NotificationAdapter extends ArrayAdapter<Notification> {

    /**
     * Constructor for the {@link NotificationAdapter}.
     *
     * @param context       The context of the calling activity or application.
     * @param notifications The list of {@link Notification} objects to display.
     */
    public NotificationAdapter(Context context, List<Notification> notifications) {
        super(context, 0, notifications);
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
        Notification notification = getItem(position); // Retrieve the current Notification instance

        if (convertView == null) {
            // Inflate a simple layout for the notification item
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        // Retrieve and set the TextViews in the layout
        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        // Set the notification's time and content
        text1.setText(notification.getTime());
        text2.setText(notification.getContent());

        return convertView;
    }
}