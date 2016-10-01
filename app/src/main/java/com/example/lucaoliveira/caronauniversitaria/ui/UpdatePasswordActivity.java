package com.example.lucaoliveira.caronauniversitaria.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lucaoliveira.caronauniversitaria.Constants;
import com.example.lucaoliveira.caronauniversitaria.R;
import com.example.lucaoliveira.caronauniversitaria.dao.UserDao;
import com.example.lucaoliveira.caronauniversitaria.model.User;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServiceTask;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServicesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by lucaoliveira on 9/22/2016.
 */
public class UpdatePasswordActivity extends AppCompatActivity {

    private UserUpdatePasswordTask mUserUpdatePasswordTask = null;

    private EditText mNewPassword;
    private EditText mConfirmNewPassword;
    private EditText mCurrentEmail;

    private UserDao userDao;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        initVariables();
        userDao = new UserDao(getBaseContext());
    }

    private void showProgress(boolean isShow) {
        findViewById(R.id.progressUpdatePassword).setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void attemptUpdatePassword(View view) {
        mCurrentEmail.setError(null);
        mNewPassword.setError(null);
        mConfirmNewPassword.setError(null);

        String currentEmail = mCurrentEmail.getText().toString();
        String password = mNewPassword.getText().toString();
        String passwordConfirmed = mConfirmNewPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(currentEmail)) {
            mCurrentEmail.setError(getString(R.string.error_field_required));
            focusView = mCurrentEmail;
            cancel = true;
        } else if (!isEmailValid(currentEmail)) {
            mCurrentEmail.setError(getString(R.string.error_invalid_email));
            focusView = mCurrentEmail;
            cancel = true;
        }

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mNewPassword.setError(getString(R.string.error_passoword_length));
            focusView = mNewPassword;
            cancel = true;
        } else if (!TextUtils.isEmpty(passwordConfirmed) && !isPasswordValid(passwordConfirmed)) {
            mConfirmNewPassword.setError(getString(R.string.error_passoword_length));
            focusView = mConfirmNewPassword;
            cancel = true;
        }

        if (!password.equalsIgnoreCase(passwordConfirmed)) {
            mConfirmNewPassword.setError(getString(R.string.error_passoword_match));
            focusView = mConfirmNewPassword;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mUserUpdatePasswordTask = new UserUpdatePasswordTask();
            mUserUpdatePasswordTask.execute();
        }
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private User getUserByEmail() {
        User user = userDao.getUserByEmail(mCurrentEmail.getText().toString());
        if (user != null) {
            return user;
        }
        return null;
    }

    private void initVariables() {
        mCurrentEmail = (EditText) findViewById(R.id.confirm_current_email_password);
        mNewPassword = (EditText) findViewById(R.id.password_update);
        mConfirmNewPassword = (EditText) findViewById(R.id.password_update_confirm);
    }

    private abstract class ActivityWebServiceTask extends WebServiceTask {
        public ActivityWebServiceTask(WebServiceTask webServiceTask) {
            super(UpdatePasswordActivity.this);
        }

        @Override
        public void showProgress() {
            UpdatePasswordActivity.this.showProgress(true);
        }

        @Override
        public void hideProgress() {
            UpdatePasswordActivity.this.showProgress(false);
        }

        @Override
        public void performSuccessfulOperation() {
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(getBaseContext(), "Senha atualizada :) ", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getBaseContext(), "O email digitado está incorreto", Toast.LENGTH_SHORT).show();
                UpdatePasswordActivity.this.showProgress(false);
            }
        }
    }

    public class UserUpdatePasswordTask extends ActivityWebServiceTask {
        public UserUpdatePasswordTask() {
            super(mUserUpdatePasswordTask);
        }

        public boolean performRequest() {
            ContentValues contentValues = new ContentValues();
            user = getUserByEmail();
            if (user != null) {
                user.setPassword(mConfirmNewPassword.getText().toString());
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put(Constants.GRANT_TYPE, Constants.CLIENT_CREDENTIALS);
                JSONObject accessTokenObject = WebServicesUtils.requestJSONObject(Constants.GENERATE_ACCESS_TOKEN_URL, WebServicesUtils.METHOD.POST, contentValues1, true);

                if (!hasError(accessTokenObject)) {
                    contentValues.put(Constants.ID, user.getId());
                    contentValues.put(Constants.PASSWORD, user.getPassword());

                    ContentValues urlValues = new ContentValues();
                    urlValues.put(Constants.ACCESS_TOKEN, accessTokenObject.optJSONObject(Constants.ACCESS).optString(Constants.ACCESS_TOKEN));

                    JSONObject obj = WebServicesUtils.requestJSONObject(Constants.UPDATE_PASSOWRD_URL, WebServicesUtils.METHOD.POST, urlValues, contentValues);

                    if (!hasError(obj)) {
                        JSONArray jsonArray = obj.optJSONArray(Constants.INFO);
                        JSONObject jsonObject = jsonArray.optJSONObject(0);
                        user.setPassword(jsonObject.optString(Constants.PASSWORD));
                        userDao.updatePassword(user);
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
    }

}
