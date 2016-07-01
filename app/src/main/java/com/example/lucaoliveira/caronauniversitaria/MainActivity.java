package com.example.lucaoliveira.caronauniversitaria;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lucaoliveira.caronauniversitaria.data.User;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServiceTask;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServicesUtils;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    String[] universityList = {"FIAP", "USP", "Uninove", "UNIP"};

    private UserLoginRegisterTask mUserLoginRegisterTask = null;

    private EditText mEmailText;
    private EditText mPasswordText;
    private EditText mNameText;
    private EditText mPhoneNumberText;
    private EditText mUniversityText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        showProgress(true);
    }

    private void showProgress(final boolean isShow) {
        findViewById(R.id.progress).setVisibility(isShow ? View.VISIBLE : View.GONE);
        findViewById(R.id.info_form).setVisibility(isShow ? View.GONE : View.VISIBLE);
    }

    private void initViews() {
        mEmailText = (EditText) findViewById(R.id.email);
        mPasswordText = (EditText) findViewById(R.id.password);
        mNameText = (EditText) findViewById(R.id.name);
        mPhoneNumberText = (EditText) findViewById(R.id.phonenumber);
        mUniversityText = (EditText) findViewById(R.id.university);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, universityList);
        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
                findViewById(R.id.university);
        materialDesignSpinner.setAdapter(arrayAdapter);
    }

    public void attemptLoginRegister(View view) {
        if (mUserLoginRegisterTask != null) {
            return;
        }

        mEmailText.setError(null);
        mPasswordText.setError(null);

        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();
        String name = mNameText.getText().toString();
        String phoneNumber = mPhoneNumberText.getText().toString();
        String university = mUniversityText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordText.setError(getString(R.string.error_passoword_length));
            focusView = mPasswordText;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailText.setError(getString(R.string.error_field_required));
            focusView = mEmailText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailText.setError(getString(R.string.error_invalid_email));
            focusView = mEmailText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mUserLoginRegisterTask = new UserLoginRegisterTask(email, password, name, university, phoneNumber, view.getId() == R.id.email_sign_in_button);
            mUserLoginRegisterTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private class UserLoginRegisterTask extends WebServiceTask {
        private final ContentValues contentValues = new ContentValues();
        private boolean mIsLogin;

        UserLoginRegisterTask(String email, String password, String name, String university, String phoneNumber, boolean isLogin) {
            super((MainActivity.this));
            contentValues.put(Constants.EMAIL, email);
            contentValues.put(Constants.PASSWORD, password);
            contentValues.put(Constants.NAME, name);
            contentValues.put(Constants.UNIVERSITY, university);
            contentValues.put(Constants.GRANT_TYPE, Constants.CLIENT_CREDENTIALS);
            mIsLogin = isLogin;
        }

        @Override
        public void showProgress() {
            MainActivity.this.showProgress(true);
        }

        @Override
        public boolean performRequest() {
            JSONObject obj = WebServicesUtils.requestJSONObject(mIsLogin ? Constants.LOGIN_URL : Constants.SIGNUP_URL,
                    WebServicesUtils.METHOD.POST, contentValues, true);
            mUserLoginRegisterTask = null;
            if (!hasError(obj)) {
                if (mIsLogin) {
                    User user = new User();
                    user.setId(obj.optLong(Constants.ID));
                    user.setEmail(contentValues.getAsString(Constants.EMAIL));
                    user.setPassword(contentValues.getAsString(Constants.PASSWORD));
                    user.setName(contentValues.getAsString(Constants.NAME));
                    user.setUniversity(contentValues.getAsString(Constants.UNIVERSITY));
                    user.setPhoneNumber(contentValues.getAsString(Constants.PHONE_NUMBER));
                    RESTServiceApplication.getInstance().setUser(user);
                    RESTServiceApplication.getInstance().setAccessToken(
                            obj.optJSONObject(Constants.ACCESS).optString(Constants.ACCESS_TOKEN));
                    return true;
                } else {
                    mIsLogin = true;
                    performRequest();
                    return true;
                }
            }
            return false;
        }

        @Override
        public void performSuccessfulOperation() {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        @Override
        public void hideProgress() {
            MainActivity.this.showProgress(false);
        }
    }


}
