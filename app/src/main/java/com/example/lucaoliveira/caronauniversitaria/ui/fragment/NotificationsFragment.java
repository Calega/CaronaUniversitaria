package com.example.lucaoliveira.caronauniversitaria.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.lucaoliveira.caronauniversitaria.R;
import com.example.lucaoliveira.caronauniversitaria.model.Notification;
import com.example.lucaoliveira.caronauniversitaria.ui.adapter.NotificationAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucaoliveira on 18/10/16.
 */

public class NotificationsFragment extends Fragment {
    private List<Notification> notificationList;
    private NotificationAdapter adapter;
    private ListView listView;

    private Notification n;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        listView = (ListView) view.findViewById(R.id.notification_listview);
        notificationList = new ArrayList<>();
        adapter = new NotificationAdapter(getActivity().getApplicationContext(), notificationList);
        listView.setAdapter(adapter);

        createAdapter();
        return view;
    }

    private void createAdapter() {
        n = new Notification("Subway", "teste", 2016, "Avenida Lins de Vasconcelos", "Venha conhecer os nossos produtos");
        notificationList.add(n);

        adapter.notifyDataSetChanged();
    }
}
