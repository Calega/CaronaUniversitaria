package com.example.lucaoliveira.caronauniversitaria.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lucaoliveira.caronauniversitaria.R;

/**
 * Created by lucaoliveira on 18/10/16.
 * Had to delete the custom adapter. This fragment is opening an "Hardcoded" list.
 * The main idea is to create an custom adapter based on "Notification" model to receive an image, customer name, address, notification message from an customer
 * This code is already developed in one of the commits. It's necessary to test and make it consistency to an interface.
 */

public class NotificationsFragment extends Fragment {

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

        View view = inflater.inflate(R.layout.notification_row, container, false);
        return view;
    }
}
