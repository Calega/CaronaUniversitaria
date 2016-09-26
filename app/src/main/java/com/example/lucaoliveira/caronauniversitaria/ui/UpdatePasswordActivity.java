package com.example.lucaoliveira.caronauniversitaria.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.lucaoliveira.caronauniversitaria.Constants;
import com.example.lucaoliveira.caronauniversitaria.R;
import com.example.lucaoliveira.caronauniversitaria.RESTServiceApplication;
import com.example.lucaoliveira.caronauniversitaria.data.User;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServiceTask;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServicesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by lucaoliveira on 9/22/2016.
 */
public class    UpdatePasswordActivity extends AppCompatActivity {

    private UserUpdatePasswordTask mUserUpdatePasswordTask = null;

    private EditText mNewPassword;
    private EditText mConfirmNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        initVariables();
    }

    private void showProgress(boolean isShow) {
        findViewById(R.id.progressUpdatePassword).setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void attemptUpdatePassword(View view) {
        if (mUserUpdatePasswordTask != null) {
            return;
        }

        mNewPassword.setError(null);
        mConfirmNewPassword.setError(null);

        String password = mNewPassword.getText().toString();
        String passwordConfirmed = mConfirmNewPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mNewPassword.setError(getString(R.string.error_passoword_length));
            focusView = mNewPassword;
            cancel = true;
        } else if (!TextUtils.isEmpty(passwordConfirmed) && !isPasswordValid(passwordConfirmed)) {
            mConfirmNewPassword.setError(getString(R.string.error_passoword_length));
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

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void populateText(User user) {
        user.setPassword(mConfirmNewPassword.getText().toString());
    }

    private void initVariables() {
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
        }
    }

    public class UserUpdatePasswordTask extends ActivityWebServiceTask {
        public UserUpdatePasswordTask() {
            super(mUserUpdatePasswordTask);
        }

        public boolean performRequest() {
            ContentValues contentValues = new ContentValues();
            User user = RESTServiceApplication.getInstance().getUser();
            populateText(user);
            contentValues.put(Constants.ID, user.getId());
            contentValues.put(Constants.PASSWORD, user.getPassword());

            ContentValues urlValues = new ContentValues();
            urlValues.put(Constants.ACCESS_TOKEN, RESTServiceApplication.getInstance().getAccessToken());

            JSONObject obj = WebServicesUtils.requestJSONObject(Constants.UPDATE_URL, WebServicesUtils.METHOD.POST, urlValues, contentValues);

            if (!hasError(obj)) {
                JSONArray jsonArray = obj.optJSONArray(Constants.INFO);
                JSONObject jsonObject = jsonArray.optJSONObject(0);
                user.setPassword(jsonObject.optString(Constants.PASSWORD));
                return true;
            }
            return false;
        }
    }

}
