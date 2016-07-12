package com.example.lucaoliveira.caronauniversitaria;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.lucaoliveira.caronauniversitaria.data.User;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServiceTask;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServicesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Lucas Calegari A. De Oliveira on 7/1/2016.
 */
public class FinishRegisterActivity extends AppCompatActivity {

    private UserEditTask mUserEditTask = null;

    private EditText mAccessType;
    private EditText mAddressOrigin;
    private EditText mAddressDestiny;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_continue);
        initViews();
//        showProgress(true);
    }

    public void finishLogin(View view) {
        if (mUserEditTask != null) {
            return;
        }

        mAccessType.setError(null);
        mAddressOrigin.setError(null);
        mAddressDestiny.setError(null);

        String accessType = mAccessType.getText().toString();
        String addressOrigin = mAddressOrigin.getText().toString();
        String addressDestiny = mAddressDestiny.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(accessType)) {
            mAccessType.setError(getString(R.string.error_accessType));
            focusView = mAccessType;
            cancel = true;
        }

        if (TextUtils.isEmpty(addressOrigin)) {
            mAddressOrigin.setError(getString(R.string.error_addressOrigin));
            focusView = mAddressOrigin;
            cancel = true;
        }

        if (TextUtils.isEmpty(addressDestiny)) {
            mAddressDestiny.setError(getString(R.string.error_addressDestiny));
            focusView = mAddressDestiny;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mUserEditTask = new UserEditTask();
            mUserEditTask.execute();
        }
    }

    private void showProgress(boolean isShow) {
        findViewById(R.id.progress).setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void initViews() {
        mAccessType = (EditText) findViewById(R.id.accessType);
        mAddressOrigin = (EditText) findViewById(R.id.addressOrigin);
        mAddressDestiny = (EditText) findViewById(R.id.addressDestiny);
    }

    private abstract class ActivityWebServiceTask extends WebServiceTask {
        public ActivityWebServiceTask(WebServiceTask webServiceTask) {
            super(FinishRegisterActivity.this);
        }

        @Override
        public void showProgress() {
            FinishRegisterActivity.this.showProgress(true);
        }

        @Override
        public void hideProgress() {
            FinishRegisterActivity.this.showProgress(false);
        }

        @Override
        public void performSuccessfulOperation() {
        }
    }

    public class UserEditTask extends ActivityWebServiceTask {
        public UserEditTask() {
            super(mUserEditTask);
        }

        public boolean performRequest() {
            ContentValues contentValues = new ContentValues();
            User user = RESTServiceApplication.getInstance().getUser();
            contentValues.put(Constants.ID, user.getId());
            contentValues.put(Constants.ACCESS_TYPE, user.getAccessType());
            contentValues.put(Constants.ADDRESS_ORIGIN, user.getAddressOrigin());
            contentValues.put(Constants.ADDRESS_DESTINY, user.getAddressDestiny());

            ContentValues urlValues = new ContentValues();
            urlValues.put(Constants.ACCESS_TOKEN, RESTServiceApplication.getInstance().getAccessToken());

            JSONObject obj = WebServicesUtils.requestJSONObject(Constants.UPDATE_URL, WebServicesUtils.METHOD.POST, urlValues, contentValues);

            if (!hasError(obj)) {
                JSONArray jsonArray = obj.optJSONArray(Constants.INFO);
                JSONObject jsonObject = jsonArray.optJSONObject(0);
                user.setAccessType(jsonObject.optString(Constants.ACCESS_TYPE));
                user.setAddressOrigin(jsonObject.optString(Constants.ADDRESS_ORIGIN));
                user.setAddressDestiny(jsonObject.optString(Constants.ADDRESS_DESTINY));
                return true;
            }
            return false;
        }
    }
}
