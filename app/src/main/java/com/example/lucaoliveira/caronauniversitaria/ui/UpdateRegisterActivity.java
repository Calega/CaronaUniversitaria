package com.example.lucaoliveira.caronauniversitaria.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.lucaoliveira.caronauniversitaria.Constants;
import com.example.lucaoliveira.caronauniversitaria.R;
import com.example.lucaoliveira.caronauniversitaria.dao.UserDao;
import com.example.lucaoliveira.caronauniversitaria.model.User;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServiceTask;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServicesUtils;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by lucaoliveira on 9/22/2016.
 */
public class UpdateRegisterActivity extends AppCompatActivity {
    String[] universityList = {"FIAP"};
    private UserUpdateRegisterTask mUserUpdateRegisterTask = null;

    private EditText mCurrentEmail;
    private EditText mNewName;
    private EditText mConfirmNewPhoneNumber;
    private EditText mConfirmNewUniversity;
    private EditText mConfirmNewAddressOrigin;
    private EditText mConfirmNewAddressDestiny;
    private EditText mConfirmNewAccessType;
    private EditText mConfirmNewStudentsAllowed;
    private EditText mConfirmNewRegisterStudentRegister;

    private UserDao userDao;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_register);
        initVariables();
        userDao = new UserDao(getBaseContext());
    }

    private void showProgress(boolean isShow) {
        findViewById(R.id.progressUpdateRegister).setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void attemptUpdateRegister(View view) {
        mCurrentEmail.setError(null);

        String currentEmail = mCurrentEmail.getText().toString();
        String phoneNumber = mConfirmNewPhoneNumber.getText().toString();

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
        } else if (TextUtils.isEmpty(phoneNumber)) {
            mConfirmNewPhoneNumber.setError(getString(R.string.error_phone_required));
            focusView = mConfirmNewPhoneNumber;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mUserUpdateRegisterTask = new UserUpdateRegisterTask();
            mUserUpdateRegisterTask.execute();
        }
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private User getUserByEmail() {
        User user = userDao.getUserByEmail(mCurrentEmail.getText().toString());
        if (user != null) {
            return user;
        }
        return null;
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

    private void initVariables() {
        mCurrentEmail = (EditText) findViewById(R.id.confirm_current_email_register);
        mNewName = (EditText) findViewById(R.id.name_update);
        mConfirmNewPhoneNumber = (EditText) findViewById(R.id.phonenumber_update);
        mConfirmNewUniversity = (EditText) findViewById(R.id.university_update);
        mConfirmNewAddressOrigin = (EditText) findViewById(R.id.addressOrigin_update);
        mConfirmNewAddressDestiny = (EditText) findViewById(R.id.addressDestiny_update);
        mConfirmNewAccessType = (EditText) findViewById(R.id.accessType_update);
        mConfirmNewStudentsAllowed = (EditText) findViewById(R.id.number_students_allowed_update);
        mConfirmNewRegisterStudentRegister = (EditText) findViewById(R.id.student_register_update);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, universityList);
        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
                findViewById(R.id.university_update);
        materialDesignSpinner.setAdapter(arrayAdapter);
    }

    /**
     * Method Name : controlRegisterUpdate
     * Type : Void
     * Param : user
     * Description : Is not required to update all data in the form. That's why we need to check whick one is NULL or BLANK to not define it in the database
     */
    private void controlRegisterUpdate(User user) {
        if (!mNewName.getText().toString().equals("") && mNewName.getText().toString() != null) {
            user.setName(mNewName.getText().toString());
        }
        if (!mConfirmNewPhoneNumber.getText().toString().equals("") && mConfirmNewPhoneNumber.getText().toString() != null) {
            user.setPhoneNumber(mConfirmNewPhoneNumber.getText().toString());
        }
        if (!mConfirmNewUniversity.getText().toString().equals("") && mConfirmNewUniversity.getText().toString() != null) {
            user.setUniversity(mConfirmNewUniversity.getText().toString());
        }
        if (!mConfirmNewAddressOrigin.getText().toString().equals("") && mConfirmNewAddressOrigin.getText().toString() != null) {
            user.setAddressOrigin(mConfirmNewAddressOrigin.getText().toString());
        }
        if (!mConfirmNewAddressDestiny.getText().toString().equals("") && mConfirmNewAddressDestiny.getText().toString() != null) {
            user.setAddressDestiny(mConfirmNewAddressDestiny.getText().toString());
        }
        if (!mConfirmNewAccessType.getText().toString().equals("") && mConfirmNewAccessType.getText().toString() != null) {
            user.setAccessType(mConfirmNewAccessType.getText().toString());
        }
        if (!mConfirmNewStudentsAllowed.getText().toString().equals("") && mConfirmNewStudentsAllowed.getText().toString() != null) {
            user.setNumberOfStudentsAllowed(Integer.valueOf(mConfirmNewStudentsAllowed.getText().toString()));
        }
        if (!mConfirmNewRegisterStudentRegister.getText().toString().equals("") && mConfirmNewRegisterStudentRegister.getText().toString() != null) {
            user.setStudentRegister(mConfirmNewRegisterStudentRegister.getText().toString());
        }
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
            if (success) {
                Toast.makeText(getBaseContext(), "Registro atualizado :) ", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getBaseContext(), "O email digitado está incorreto", Toast.LENGTH_SHORT).show();
                UpdateRegisterActivity.this.showProgress(false);
            }
        }
    }

    public class UserUpdateRegisterTask extends ActivityWebServiceTask {
        public UserUpdateRegisterTask() {
            super(mUserUpdateRegisterTask);
        }

        public boolean performRequest() {
            ContentValues contentValues = new ContentValues();
            user = getUserByEmail();
            if (user != null) {
                controlRegisterUpdate(user);
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put(Constants.GRANT_TYPE, Constants.CLIENT_CREDENTIALS);
                JSONObject accessTokenObject = WebServicesUtils.requestJSONObject(Constants.GENERATE_ACCESS_TOKEN_URL, WebServicesUtils.METHOD.POST, contentValues1, true);

                if (!hasError(accessTokenObject)) {
                    contentValues.put(Constants.ID, user.getId());
                    contentValues.put(Constants.NAME, user.getName());
                    contentValues.put(Constants.PHONE_NUMBER, user.getPhoneNumber());
                    contentValues.put(Constants.UNIVERSITY, user.getUniversity());
                    contentValues.put(Constants.ADDRESS_ORIGIN, user.getAddressOrigin());
                    contentValues.put(Constants.ADDRESS_DESTINY, user.getAddressDestiny());
                    contentValues.put(Constants.ACCESS_TYPE, user.getAccessType());
                    contentValues.put(Constants.STUDENTS_ALLOWED, user.getNumberOfStudentsAllowed());
                    contentValues.put(Constants.STUDENT_REGISTER, user.getStudentRegister());

                    ContentValues urlValues = new ContentValues();
                    urlValues.put(Constants.ACCESS_TOKEN, accessTokenObject.optJSONObject(Constants.ACCESS).optString(Constants.ACCESS_TOKEN));

                    JSONObject obj = WebServicesUtils.requestJSONObject(Constants.UPDATE_REGISTER_URL, WebServicesUtils.METHOD.POST, urlValues, contentValues);

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
                        user.setStudentRegister(jsonObject.optString(Constants.STUDENT_REGISTER));
                        userDao.updateEmail(user);
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
