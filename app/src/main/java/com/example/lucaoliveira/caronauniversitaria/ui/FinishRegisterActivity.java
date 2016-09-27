package com.example.lucaoliveira.caronauniversitaria.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.lucaoliveira.caronauniversitaria.Constants;
import com.example.lucaoliveira.caronauniversitaria.R;
import com.example.lucaoliveira.caronauniversitaria.RESTServiceApplication;
import com.example.lucaoliveira.caronauniversitaria.model.User;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServiceTask;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServicesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Lucas Calegari A. De Oliveira on 7/1/2016.
 */
public class FinishRegisterActivity extends AppCompatActivity {

    // TODO - RETIRAR FOTO E SALVAR EM USER THUMNAIL

    private UserEditTask mUserEditTask = null;

    private EditText mAccessType;
    private EditText mAddressOrigin;
    private EditText mAddressDestiny;
    private EditText mStudentsAllowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_register);
        initViews();
    }

    public void finishLogin(View view) {
        if (mUserEditTask != null) {
            return;
        }

        mAccessType.setError(null);
        mAddressOrigin.setError(null);
        mAddressDestiny.setError(null);
        mStudentsAllowed.setError(null);

        String accessType = mAccessType.getText().toString();
        String addressOrigin = mAddressOrigin.getText().toString();
        String addressDestiny = mAddressDestiny.getText().toString();
        String studentsAllowed = mStudentsAllowed.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(accessType)) {
            mAccessType.setError(getString(R.string.error_accessType));
            focusView = mAccessType;
            cancel = true;
        }

        if (accessType.equals("Caroneiro") && TextUtils.isEmpty(studentsAllowed)) {
            mStudentsAllowed.setError(getString(R.string.error_accessType_caroneiro));
            focusView = mStudentsAllowed;
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
        mStudentsAllowed = (EditText) findViewById(R.id.number_students_allowed);
        mAccessType = (EditText) findViewById(R.id.accessType);
        mAddressOrigin = (EditText) findViewById(R.id.addressOrigin);
        mAddressDestiny = (EditText) findViewById(R.id.addressDestiny);
    }

    private void populateText(User user) {
        user.setAccessType(mAccessType.getText().toString());
        if (!mStudentsAllowed.getText().toString().equals("") && mStudentsAllowed.getText().toString() != null) {
            user.setNumberOfStudentsAllowed(Integer.valueOf(mStudentsAllowed.getText().toString()));
        } else {
            user.setNumberOfStudentsAllowed(0);
        }
        user.setAddressOrigin(mAddressOrigin.getText().toString());
        user.setAddressDestiny(mAddressDestiny.getText().toString());
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_caroneiro:
                if (checked)
                    mAccessType.setText("Caroneiro");
                mStudentsAllowed.setVisibility(View.VISIBLE);
                break;
            case R.id.radio_carona:
                if (checked)
                    mAccessType.setText("Carona");
                mStudentsAllowed.setVisibility(View.GONE);
                break;
        }
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

        @Override
        protected void onPostExecute(Boolean success) {
            Log.d("FinishRegisterActivity", "Successful ?!" + success);
            if (success) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.prompt_welcome), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), StudentsActivity.class);
                startActivity(intent);
            }
        }
    }

    public class UserEditTask extends ActivityWebServiceTask {
        public UserEditTask() {
            super(mUserEditTask);
        }

        public boolean performRequest() {
            ContentValues contentValues = new ContentValues();
            User user = RESTServiceApplication.getInstance().getUser();
            populateText(user);
            contentValues.put(Constants.ID, user.getId());
            contentValues.put(Constants.ACCESS_TYPE, user.getAccessType());
            contentValues.put(Constants.ADDRESS_ORIGIN, user.getAddressOrigin());
            contentValues.put(Constants.ADDRESS_DESTINY, user.getAddressDestiny());
            contentValues.put(Constants.STUDENTS_ALLOWED, user.getNumberOfStudentsAllowed());

            ContentValues urlValues = new ContentValues();
            urlValues.put(Constants.ACCESS_TOKEN, RESTServiceApplication.getInstance().getAccessToken());

            JSONObject obj = WebServicesUtils.requestJSONObject(Constants.UPDATE_URL, WebServicesUtils.METHOD.POST, urlValues, contentValues);

            if (!hasError(obj)) {
                JSONArray jsonArray = obj.optJSONArray(Constants.INFO);
                JSONObject jsonObject = jsonArray.optJSONObject(0);
                user.setAccessType(jsonObject.optString(Constants.ACCESS_TYPE));
                user.setAddressOrigin(jsonObject.optString(Constants.ADDRESS_ORIGIN));
                user.setAddressDestiny(jsonObject.optString(Constants.ADDRESS_DESTINY));
                user.setNumberOfStudentsAllowed(jsonObject.optInt(Constants.STUDENTS_ALLOWED));
                return true;
            }
            return false;
        }
    }
}
