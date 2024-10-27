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

public class ItemAdapter extends ArrayAdapter<Item> {
    private Context context;

    public ItemAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position); // 获取当前的Item实例
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            // 加载自定义布局
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.academicYear = view.findViewById(R.id.academicYear);
            viewHolder.totalFee = view.findViewById(R.id.totalFee);
            viewHolder.major = view.findViewById(R.id.major);
            viewHolder.createdTime = view.findViewById(R.id.createdTime);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }

        // 设置名称、价格
        viewHolder.academicYear.setText(item.getAcademicYear());
        viewHolder.createdTime.setText(item.getCreatedTime());
        viewHolder.major.setText(item.getMajor());
        viewHolder.totalFee.setText("$" + String.valueOf(item.getTotalFee()));





        return view;
    }

    // ViewHolder 用于缓存控件
    static class ViewHolder {
        TextView academicYear;
        TextView totalFee;
        TextView createdTime;
        TextView major;
    }
}
