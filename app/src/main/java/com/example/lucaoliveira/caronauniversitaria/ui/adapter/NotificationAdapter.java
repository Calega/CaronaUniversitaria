package com.example.lucaoliveira.caronauniversitaria.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lucaoliveira.caronauniversitaria.R;
import com.example.lucaoliveira.caronauniversitaria.model.Notification;

import java.util.List;

/**
 * Created by lucaoliveira on 19/10/16.
 */

public class NotificationAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<Notification> notificationList;

    public NotificationAdapter(Context mContext, List<Notification> notificationList) {
        this.mContext = mContext;
        this.notificationList = notificationList;
    }

    @Override
    public int getCount() {
        return notificationList.size();
    }

    @Override
    public Object getItem(int i) {
        return notificationList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.notification_row, null);


        ImageView customerImage = (ImageView) view.findViewById(R.id.thumbnail);
        TextView customer = (TextView) view.findViewById(R.id.customerName);
        TextView address = (TextView) view.findViewById(R.id.addressName);
        TextView notificationText = (TextView) view.findViewById(R.id.notificationText);
        TextView year = (TextView) view.findViewById(R.id.releaseYear);

        Notification notification = notificationList.get(i);

//        Bitmap bitmap = decodeFromBase64(notification.getThumbnailUrl());
//        customerImage.setImageBitmap(bitmap);

        customer.setText(notification.getCustomer());
        address.setText(notification.getAddress());
        notificationText.setText(notification.getNotificationText());
        year.setText(String.valueOf(notification.getYear()));

        return view;
    }

    public static Bitmap decodeFromBase64(String image) {
        byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

}
