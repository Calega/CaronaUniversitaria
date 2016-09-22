package com.example.lucaoliveira.caronauniversitaria.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

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
public class UpdateRegisterActivity extends AppCompatActivity {

    private UserUpdateRegisterTask mUserUpdateRegisterTask = null;

    private EditText mNewName;
    private EditText mConfirmNewPhoneNumber;
    private EditText mConfirmNewUniversity;
    private EditText mConfirmNewAddressOrigin;
    private EditText mConfirmNewAddressDestiny;
    private EditText mConfirmNewAccessType;
    private EditText mConfirmNewStudentsAllowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        initVariables();
    }

    private void showProgress(boolean isShow) {
        findViewById(R.id.progressUpdatePassword).setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void attemptUpdateEmail(View view) {
        if (mUserUpdateRegisterTask != null) {
            return;
        }
        mUserUpdateRegisterTask = new UserUpdateRegisterTask();
        mUserUpdateRegisterTask.execute();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_caroneiro_update:
                if (checked)
                    mConfirmNewAccessType.setText("Caroneiro");
                mConfirmNewStudentsAllowed.setVisibility(View.VISIBLE);
                break;
            case R.id.radio_carona_update:
                if (checked)
                    mConfirmNewAccessType.setText("Carona");
                mConfirmNewStudentsAllowed.setVisibility(View.GONE);
                break;
        }
    }

    private void populateText(User user) {
        user.setAccessType(mConfirmNewAccessType.getText().toString());
        user.setName(mNewName.getText().toString());
        if (!mConfirmNewStudentsAllowed.getText().toString().equals("") && mConfirmNewStudentsAllowed.getText().toString() != null) {
            user.setNumberOfStudentsAllowed(Integer.valueOf(mConfirmNewStudentsAllowed.getText().toString()));
        } else {
            user.setNumberOfStudentsAllowed(0);
        }
        user.setAddressOrigin(mConfirmNewAddressOrigin.getText().toString());
        user.setAddressDestiny(mConfirmNewAddressDestiny.getText().toString());
        user.setPhoneNumber(mConfirmNewPhoneNumber.getText().toString());
        user.setUniversity(mConfirmNewUniversity.getText().toString());
    }

    private void initVariables() {
        mNewName = (EditText) findViewById(R.id.name_update);
        mConfirmNewPhoneNumber = (EditText) findViewById(R.id.phonenumber_update);
        mConfirmNewUniversity = (EditText) findViewById(R.id.university_update);
        mConfirmNewAddressOrigin = (EditText) findViewById(R.id.addressOrigin_update);
        mConfirmNewAddressDestiny = (EditText) findViewById(R.id.addressDestiny_update);
        mConfirmNewAccessType = (EditText) findViewById(R.id.accessType_update);
        mConfirmNewStudentsAllowed = (EditText) findViewById(R.id.number_students_allowed_update);
    }

    private abstract class ActivityWebServiceTask extends WebServiceTask {
        public ActivityWebServiceTask(WebServiceTask webServiceTask) {
            super(UpdateRegisterActivity.this);
        }

        @Override
        public void showProgress() {
            UpdateRegisterActivity.this.showProgress(true);
        }

        @Override
        public void hideProgress() {
            UpdateRegisterActivity.this.showProgress(false);
        }

        @Override
        public void performSuccessfulOperation() {
        }

        @Override
        protected void onPostExecute(Boolean success) {
        }
    }

    public class UserUpdateRegisterTask extends ActivityWebServiceTask {
        public UserUpdateRegisterTask() {
            super(mUserUpdateRegisterTask);
        }

        public boolean performRequest() {
            ContentValues contentValues = new ContentValues();
            User user = RESTServiceApplication.getInstance().getUser();
            populateText(user);
            contentValues.put(Constants.ID, user.getId());
            contentValues.put(Constants.NAME, user.getName());
            contentValues.put(Constants.PHONE_NUMBER, user.getPhoneNumber());
            contentValues.put(Constants.UNIVERSITY, user.getUniversity());
            contentValues.put(Constants.ADDRESS_ORIGIN, user.getAddressOrigin());
            contentValues.put(Constants.ADDRESS_DESTINY, user.getAddressDestiny());
            contentValues.put(Constants.ACCESS_TYPE, user.getAccessType());
            contentValues.put(Constants.STUDENTS_ALLOWED, user.getNumberOfStudentsAllowed());

            ContentValues urlValues = new ContentValues();
            urlValues.put(Constants.ACCESS_TOKEN, RESTServiceApplication.getInstance().getAccessToken());

            JSONObject obj = WebServicesUtils.requestJSONObject(Constants.UPDATE_URL, WebServicesUtils.METHOD.POST, urlValues, contentValues);

            if (!hasError(obj)) {
                JSONArray jsonArray = obj.optJSONArray(Constants.INFO);
                JSONObject jsonObject = jsonArray.optJSONObject(0);
                user.setName(jsonObject.optString(Constants.NAME));
                user.setPhoneNumber(jsonObject.optString(Constants.PHONE_NUMBER));
                user.setUniversity(jsonObject.optString(Constants.UNIVERSITY));
                user.setAddressOrigin(jsonObject.optString(Constants.ADDRESS_ORIGIN));
                user.setAddressDestiny(jsonObject.optString(Constants.ADDRESS_DESTINY));
                user.setAccessType(jsonObject.optString(Constants.ACCESS_TYPE));
                user.setNumberOfStudentsAllowed(jsonObject.optInt(Constants.STUDENTS_ALLOWED));
                return true;
            }
            return false;
        }
    }

}
