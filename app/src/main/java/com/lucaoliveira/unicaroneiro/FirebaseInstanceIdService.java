package com.lucaoliveira.unicaroneiro;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by Lucas Calegari Alves de Oliveira on 10/24/16.
 */

public class FirebaseInstanceIdService extends com.google.firebase.iid.FirebaseInstanceIdService {

    public static final String TAG = "Notificação";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "Token:" + token);
    }
}
