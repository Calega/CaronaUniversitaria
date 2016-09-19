package com.example.lucaoliveira.caronauniversitaria.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lucaoliveira.caronauniversitaria.R;

/**
 * Created by lucas on 21/08/2016.
 */
public class StudentInformationActivity extends AppCompatActivity {
    public static final String EXTRA_USER_THUMBNAIL = "user_thumbnail";
    public static final String EXTRA_USER_NAME = "user_name";
    public static final String EXTRA_USER_PHONE = "user_phone";
    public static final String EXTRA_USER_EMAIL = "user_email";
    public static final String EXTRA_USER_ADDRESS_ORIGIN = "user_origin";
    public static final String EXTRA_USER_ADDRESS_DESTINY = "user_destiny";

    private ImageView thumbail;
    private TextView name, phone, email, addressOrigin, addressDestiny;
    private Button dismissButton;

    private String userThumbnail, userName, userPhone, userEmail, userOrigin, userDestiny;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_student_information);

        initVariables();
        getIntentInformation();
        buttonControl();
    }

    private void buttonControl() {
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });
    }

    private void call() {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.getText().toString())));
    }

    private void getIntentInformation() {
        Intent intent = getIntent();
//        String userThumbnail = intent.getStringExtra(StudentInformationActivity.EXTRA_USER_THUMBNAIL);
        String userName = intent.getStringExtra(StudentInformationActivity.EXTRA_USER_NAME);
        String userPhone = intent.getStringExtra(StudentInformationActivity.EXTRA_USER_PHONE);
        String userEmail = intent.getStringExtra(StudentInformationActivity.EXTRA_USER_EMAIL);
        String userOrigin = intent.getStringExtra(StudentInformationActivity.EXTRA_USER_ADDRESS_ORIGIN);
        String userDestiny = intent.getStringExtra(StudentInformationActivity.EXTRA_USER_ADDRESS_DESTINY);

        name.setText(userName);
        phone.setText(userPhone);
        email.setText(userEmail);
        addressOrigin.setText(userOrigin);
        addressDestiny.setText(userDestiny);
    }

    private void initVariables() {
        thumbail = (ImageView) findViewById(R.id.thumbail_student_information);
        name = (TextView) findViewById(R.id.tv_student_name);
        phone = (TextView) findViewById(R.id.tv_student_phone);
        email = (TextView) findViewById(R.id.tv_student_email);
        addressOrigin = (TextView) findViewById(R.id.tv_student_address_origin);
        addressDestiny = (TextView) findViewById(R.id.tv_student_address_destiny);
        dismissButton = (Button) findViewById(R.id.finish_button);
    }
}
