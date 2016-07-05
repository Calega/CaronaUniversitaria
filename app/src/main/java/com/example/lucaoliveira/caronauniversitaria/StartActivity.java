package com.example.lucaoliveira.caronauniversitaria;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by lucas on 30/06/2016.
 */
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void clickRegister(View view) {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
