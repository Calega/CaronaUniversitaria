package com.lucaoliveira.unicaroneiro.ui.fragment;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lucaoliveira.unicaroneiro.R;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Lucas Calegari Alves de Oliveira on 18/10/16.
 * Had to delete the custom adapter. This fragment is opening an "Hardcoded" list.
 * The main idea is to create an custom adapter based on "Notification" model to receive an image, customer name, address, notification message from an customer
 * This code is already developed in one of the commits. It's necessary to test and make it consistency to an interface.
 */

public class NotificationsFragment extends Fragment {
    private RelativeLayout notification1, notification2, notification3;
    private LocationManager lm;

    private GPSReceiver receiver;

    private String latitude;
    private String longitude;

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
        initiViews(view);
        latitudeAndLongitude();
        buttonControl();
        return view;
    }

    private void latitudeAndLongitude() {
        try {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void buttonControl() {
        notification1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMapsIntent("Av. Lins de Vasconcelos, 1031");
            }
        });

        notification2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMapsIntent("Av. Lins de Vasconcelos, 1450");
            }
        });

        notification3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMapsIntent("Av. Lins de Vasconcelos, 1075");
            }
        });
    }

    private void googleMapsIntent(String address) {
        String uri = "http://maps.google.com/maps?daddr= " + address;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Toast.makeText(getActivity().getApplicationContext(), "Please install google maps application", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initiViews(View view) {
        notification1 = (RelativeLayout) view.findViewById(R.id.first_notification);
        notification2 = (RelativeLayout) view.findViewById(R.id.second_notification);
        notification3 = (RelativeLayout) view.findViewById(R.id.third_notification);

        lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        receiver = new GPSReceiver();
        IntentFilter filter = new IntentFilter("android.location.PROVIDERS_CHANGED");
        getActivity().registerReceiver(receiver, filter);
    }

    private class GPSReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (checkGPS()) {
                Toast.makeText(context, "GPS On", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "GPS Off", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latitude = String.valueOf(location.getLatitude()).replace(",", ".");
            longitude = String.valueOf(location.getLongitude()).replace(",", ".");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    private boolean checkGPS() {
        if (lm != null &&
                (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                        lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            return true;
        }

        return false;
    }
}
