package com.lucaoliveira.unicaroneiro.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.lucaoliveira.unicaroneiro.Constants;
import com.lucaoliveira.unicaroneiro.R;
import com.lucaoliveira.unicaroneiro.RESTServiceApplication;
import com.lucaoliveira.unicaroneiro.model.User;
import com.lucaoliveira.unicaroneiro.webservices.WebServiceTask;
import com.lucaoliveira.unicaroneiro.webservices.WebServicesUtils;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONObject;

/**
 * Created by Lucas Calegari A. De Oliveira on 01/07/2016.
 */
public class StartRegisterActivity extends AppCompatActivity {
    public static final String EXTRA_USER_EMAIL = "user_email";
    public static final String EXTRA_USER_NAME = "user_name";

    String[] universityList = {"FIAP"};

    private UserLoginRegisterTask mUserLoginRegisterTask = null;

    private EditText mEmail;
    private EditText mPassword;
    private EditText mName;
    private EditText mPhoneNumber;
    private EditText mUniversity;
    private EditText mStudentRegister;
    private EditText mConfirmPassword;

    private String googleEmail, googleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_register);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            googleEmail = extras.getString(StartRegisterActivity.EXTRA_USER_EMAIL);
            googleName = extras.getString(StartRegisterActivity.EXTRA_USER_NAME);
        }
        initViews();
    }


    private void showProgress(final boolean isShow) {
        findViewById(R.id.progress).setVisibility(isShow ? View.VISIBLE : View.GONE);
        findViewById(R.id.info_form_start).setVisibility(isShow ? View.GONE : View.VISIBLE);
    }

    private void initViews() {
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mName = (EditText) findViewById(R.id.name);
        mPhoneNumber = (EditText) findViewById(R.id.phonenumber);
        mUniversity = (EditText) findViewById(R.id.university);
        mStudentRegister = (EditText) findViewById(R.id.student_register_university);
        mConfirmPassword = (EditText) findViewById(R.id.confirm_password);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, universityList);
        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
                findViewById(R.id.university);
        materialDesignSpinner.setAdapter(arrayAdapter);

        if (googleEmail != null && googleEmail != "") {
            mEmail.setText(googleEmail);
        }

        if (googleName != null && googleName != "") {
            mName.setText(googleName);
        }
    }

    public void attemptLoginRegister(View view) {
        if (mUserLoginRegisterTask != null) {
            return;
        }

        mEmail.setError(null);
        mPassword.setError(null);

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();
        String name = mName.getText().toString();
        String phoneNumber = mPhoneNumber.getText().toString();
        String university = mUniversity.getText().toString();
        String registerStudent = mStudentRegister.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_passoword_length));
            focusView = mPassword;
            cancel = true;
        } else if (!TextUtils.isEmpty(confirmPassword) && !isPasswordValid(confirmPassword)) {
            mConfirmPassword.setError(getString(R.string.error_passoword_length));
            focusView = mConfirmPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_field_required));
            focusView = mEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEmail;
            cancel = true;
        }

        if (!password.equalsIgnoreCase(confirmPassword)) {
            mConfirmPassword.setError(getString(R.string.error_passoword_match));
            focusView = mConfirmPassword;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mUserLoginRegisterTask = new UserLoginRegisterTask(email, password, name, university, phoneNumber, registerStudent, view.getId() == R.id.continue_button);
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
        private boolean mIsRegister;

        UserLoginRegisterTask(String email, String password, String name, String university, String phoneNumber, String studentRegister, boolean isRegister) {
            super((StartRegisterActivity.this));
            contentValues.put(Constants.EMAIL, email);
            contentValues.put(Constants.PASSWORD, password);
            contentValues.put(Constants.NAME, name);
            contentValues.put(Constants.PHONE_NUMBER, phoneNumber);
            contentValues.put(Constants.UNIVERSITY, university);
            contentValues.put(Constants.STUDENT_REGISTER, studentRegister);
            contentValues.put(Constants.GRANT_TYPE, Constants.CLIENT_CREDENTIALS);
            mIsRegister = isRegister;
        }

        @Override
        public void showProgress() {
            StartRegisterActivity.this.showProgress(true);
        }

        @Override
        public boolean performRequest() {
            JSONObject obj = WebServicesUtils.requestJSONObject(mIsRegister ? Constants.SIGNUP_URL : Constants.LOGIN_URL,
                    WebServicesUtils.METHOD.POST, contentValues, true);
            mUserLoginRegisterTask = null;
            if (!hasError(obj)) {
                if (!mIsRegister) {
                    User user = new User();
                    user.setId(obj.optLong(Constants.ID));
                    user.setEmail(contentValues.getAsString(Constants.EMAIL));
                    user.setPassword(contentValues.getAsString(Constants.PASSWORD));
                    user.setName(contentValues.getAsString(Constants.NAME));
                    user.setUniversity(contentValues.getAsString(Constants.UNIVERSITY));
                    user.setPhoneNumber(contentValues.getAsString(Constants.PHONE_NUMBER));
                    user.setStudentRegister(contentValues.getAsString(Constants.STUDENT_REGISTER));
                    RESTServiceApplication.getInstance().setUser(user);
                    RESTServiceApplication.getInstance().setAccessToken(
                            obj.optJSONObject(Constants.ACCESS).optString(Constants.ACCESS_TOKEN));
                    return true;
                } else {
                    mIsRegister = false;
                    performRequest();
                    return true;
                }
            }
            return false;
        }

        @Override
        public void performSuccessfulOperation() {
            Intent intent = new Intent(StartRegisterActivity.this, FinishRegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        @Override
        public void hideProgress() {
            StartRegisterActivity.this.showProgress(false);
        }
    }

}
