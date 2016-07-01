package com.example.lucaoliveira.caronauniversitaria;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lucaoliveira.caronauniversitaria.data.User;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServiceTask;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServicesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by lucaoliveira on 7/1/2016.
 */
public class FinishRegisterActivity extends AppCompatActivity {

    private UserEditTask mUserEditTask = null;
    private UserInfoTask mUserInfoTask = null;

    private EditText mAccessType;
    private EditText mAddressOrigin;
    private EditText mAddressDestiny;
    private Button mFinishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        showProgress(true);
        mUserInfoTask = new UserInfoTask();
        mUserInfoTask.execute();
    }

    private void showProgress(boolean isShow) {
        findViewById(R.id.progress).setVisibility(isShow ? View.VISIBLE : View.GONE);
        findViewById(R.id.info_form).setVisibility(isShow ? View.GONE : View.VISIBLE);
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
//            populateText();
        }

//        private void populateText() {
//            User user = RESTServiceApplication.getInstance().getUser();
//            mEmailText.setText(user.getEmail());
//            mPasswordText.setText(user.getPassword());
//            mNameText.setText(user.getName() == null ? "" : user.getName());
//            mPhoneNumberText.setText(user.getPhoneNumber() == null ? "" : user.getPhoneNumber());
//        }


    }

    public class UserInfoTask extends ActivityWebServiceTask {
        public UserInfoTask() {
            super(mUserInfoTask);
        }

        public boolean performRequest() {
            ContentValues contentValues = new ContentValues();
            User user = RESTServiceApplication.getInstance().getUser();
            contentValues.put(Constants.ID, user.getId());
            contentValues.put(Constants.ACCESS_TOKEN, RESTServiceApplication.getInstance().getAccessToken());

            JSONObject object = WebServicesUtils.requestJSONObject(Constants.INFO_URL, WebServicesUtils.METHOD.GET, contentValues, null);
            if (!hasError(object)) {
                JSONArray jsonArray = object.optJSONArray(Constants.INFO); // informação vem em formato de json
                JSONObject jsonObject = jsonArray.optJSONObject(0); // pegando o index 0 do array

                user.setName(jsonObject.optString(Constants.NAME));
                if (user.getName().equalsIgnoreCase("null")) {
                    user.setName(null);
                }

                user.setPhoneNumber(jsonObject.optString(Constants.PHONE_NUMBER));
                if (user.getPhoneNumber().equalsIgnoreCase("null")) {
                    user.setPhoneNumber(null);
                }

                user.setId(jsonObject.optLong(Constants.ID_INFO));
                return true;
            }
            return false;
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
