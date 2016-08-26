package com.example.lucaoliveira.caronauniversitaria.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.lucaoliveira.caronauniversitaria.R;

/**
 * Created by lucas on 30/06/2016.
 */
public class HomeScreenActivity extends AppCompatActivity {
    public static final String TAG = HomeScreenActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }

    public void clickRegister(View view) {
        if (hasActiveInternetConnection()) {
            Intent intent = new Intent(HomeScreenActivity.this, StartRegisterActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "É necessário ter conexão com a internet!", Toast.LENGTH_SHORT).show();
        }
    }

    public void clickLogin(View view) {
        if (hasActiveInternetConnection()) {
            Intent intent = new Intent(HomeScreenActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "É necessário ter conexão com a internet!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean hasActiveInternetConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

}
