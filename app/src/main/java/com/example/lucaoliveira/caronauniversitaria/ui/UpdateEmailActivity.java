package com.example.lucaoliveira.caronauniversitaria.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.example.lucaoliveira.caronauniversitaria.Constants;
import com.example.lucaoliveira.caronauniversitaria.R;
import com.example.lucaoliveira.caronauniversitaria.RESTServiceApplication;
import com.example.lucaoliveira.caronauniversitaria.model.User;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServiceTask;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServicesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Lucas Calegari A. de Oliveira on 9/22/2016.
 */
public class UpdateEmailActivity extends AppCompatActivity {

    private UserUpdateEmailTask mUserUpdateEmailTask = null;

    private EditText mNewEmail;
    private EditText mConfirmNewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        initVariables();
    }

    private void showProgress(boolean isShow) {
        findViewById(R.id.progressUpdateEmail).setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void attemptUpdateEmail(View view) {
        if (mUserUpdateEmailTask != null) {
            return;
        }

        mNewEmail.setError(null);
        mConfirmNewEmail.setError(null);

        String email = mNewEmail.getText().toString();
        String emailConfirmed = mConfirmNewEmail.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            mNewEmail.setError(getString(R.string.error_field_required));
            focusView = mNewEmail;
            cancel = true;
        } else if (TextUtils.isEmpty(emailConfirmed)) {
            mConfirmNewEmail.setError(getString(R.string.error_field_required));
            focusView = mConfirmNewEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mNewEmail.setError(getString(R.string.error_invalid_email));
            focusView = mNewEmail;
            cancel = true;
        } else if (!isEmailValid(emailConfirmed)) {
            mConfirmNewEmail.setError(getString(R.string.error_invalid_email));
            focusView = mConfirmNewEmail;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mUserUpdateEmailTask = new UserUpdateEmailTask();
            mUserUpdateEmailTask.execute();
        }
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void populateText(User user) {
        user.setEmail(mConfirmNewEmail.getText().toString());
    }

    private void initVariables() {
        mNewEmail = (EditText) findViewById(R.id.email_update);
        mConfirmNewEmail = (EditText) findViewById(R.id.email_update_confirm);
    }

    private abstract class ActivityWebServiceTask extends WebServiceTask {
        public ActivityWebServiceTask(WebServiceTask webServiceTask) {
            super(UpdateEmailActivity.this);
        }

        @Override
        public void showProgress() {
            UpdateEmailActivity.this.showProgress(true);
        }

        @Override
        public void hideProgress() {
            UpdateEmailActivity.this.showProgress(false);
        }

        @Override
        public void performSuccessfulOperation() {
        }

        @Override
        protected void onPostExecute(Boolean success) {
        }
    }

    public class UserUpdateEmailTask extends ActivityWebServiceTask {
        public UserUpdateEmailTask() {
            super(mUserUpdateEmailTask);
        }

        public boolean performRequest() {
            ContentValues contentValues = new ContentValues();
            User user = RESTServiceApplication.getInstance().getUser();
            populateText(user);
            contentValues.put(Constants.ID, user.getId());
            contentValues.put(Constants.EMAIL, user.getEmail());

//            ContentValues urlValues = new ContentValues();
//            urlValues.put(Constants.ACCESS_TOKEN, RESTServiceApplication.getInstance().getAccessToken());

            JSONObject obj = WebServicesUtils.requestJSONObject(Constants.UPDATE_EMAIL_URL, WebServicesUtils.METHOD.POST, contentValues, true);

            if (!hasError(obj)) {
                JSONArray jsonArray = obj.optJSONArray(Constants.INFO);
                JSONObject jsonObject = jsonArray.optJSONObject(0);
                user.setEmail(jsonObject.optString(Constants.EMAIL));
                return true;
            }
            return false;
        }
    }

}
