package com.example.lucaoliveira.caronauniversitaria.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lucaoliveira.caronauniversitaria.Constants;
import com.example.lucaoliveira.caronauniversitaria.R;
import com.example.lucaoliveira.caronauniversitaria.RESTServiceApplication;
import com.example.lucaoliveira.caronauniversitaria.dao.UserDao;
import com.example.lucaoliveira.caronauniversitaria.model.User;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServiceTask;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServicesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Lucas Calegari A. De Oliveira on 01/07/2016.
 */
public class LoginActivity extends AppCompatActivity {
    private UserLoginTask mUserLoginTask = null;

    private EditText mEmailText;
    private EditText mPasswordText;

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userDao = new UserDao(getBaseContext());
        initViews();
    }

    private void initViews() {
        mEmailText = (EditText) findViewById(R.id.email_login);
        mPasswordText = (EditText) findViewById(R.id.password_login);
    }

    public void attemptLoginSignIn(View view) {

        mEmailText.setError(null);
        mPasswordText.setError(null);

        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

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
            mUserLoginTask = new UserLoginTask();
            mUserLoginTask.execute();
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void populateText(User user) {
        user.setEmail(mEmailText.getText().toString());
        user.setPassword(mPasswordText.getText().toString());
    }

    private abstract class ActivityWebServiceTask extends WebServiceTask {
        public ActivityWebServiceTask(WebServiceTask webServiceTask) {
            super(LoginActivity.this);
        }

        @Override
        public void showProgress() {
            LoginActivity.this.showProgress(true);
        }

        @Override
        public void hideProgress() {
            LoginActivity.this.showProgress(false);
        }

        @Override
        public void performSuccessfulOperation() {
        }

        @Override
        protected void onPostExecute(Boolean success) {
            Log.d("LoginActivity", "Successful ?!" + success);
            if (success) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.prompt_welcome_back), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.prompt_invalid_access), Toast.LENGTH_SHORT).show();
                LoginActivity.this.showProgress(false);
            }
        }
    }

    private void showProgress(boolean isShow) {
        findViewById(R.id.progress_login).setVisibility(isShow ? View.VISIBLE : View.GONE);
        findViewById(R.id.email_info_form).setVisibility(isShow ? View.GONE : View.VISIBLE);
    }

    public class UserLoginTask extends ActivityWebServiceTask {
        public UserLoginTask() {
            super(mUserLoginTask);
        }

        public boolean performRequest() {
            ContentValues contentValues = new ContentValues();
            User user = RESTServiceApplication.getInstance().getUser();
            populateText(user);
            contentValues.put(Constants.EMAIL, user.getEmail());
            contentValues.put(Constants.PASSWORD, user.getPassword());

            JSONObject object = WebServicesUtils.requestJSONObject(Constants.INFO_URL, WebServicesUtils.METHOD.POST, contentValues, true);

            if (!hasError(object)) {
                persistUser(object);
                return true;
            }

            return false;
        }
    }

    private void persistUser(JSONObject object) {
        User user = new User();

        JSONArray jsonArray = object.optJSONArray(Constants.INFO); // informação vem em formato de json
        JSONObject jsonObject = jsonArray.optJSONObject(0); // pegando o index 0 do array

        user.setId(jsonObject.optInt(Constants.ID));
        if (user.getId() == 0) {
            user.setId(0);
        }

        user.setName(jsonObject.optString(Constants.NAME));
        if (user.getName().equalsIgnoreCase("null")) {
            user.setName(null);
        }

        user.setEmail(jsonObject.optString(Constants.EMAIL));
        if (user.getEmail().equalsIgnoreCase("null")) {
            user.setEmail(null);
        }

        user.setPassword(jsonObject.optString(Constants.PASSWORD));
        if (user.getPassword().equalsIgnoreCase("null")) {
            user.setPassword(null);
        }

        user.setPhoneNumber(jsonObject.optString(Constants.PHONE_NUMBER));
        if (user.getPhoneNumber().equalsIgnoreCase("null")) {
            user.setPhoneNumber(null);
        }

        user.setUniversity(jsonObject.optString(Constants.UNIVERSITY));
        if (user.getUniversity().equalsIgnoreCase("null")) {
            user.setUniversity(null);
        }

        user.setAccessType(jsonObject.optString(Constants.ACCESS_TYPE));
        if (user.getAccessType().equalsIgnoreCase("null")) {
            user.setAccessType(null);
        }

        user.setAddressOrigin(jsonObject.optString(Constants.ADDRESS_ORIGIN));
        if (user.getAddressOrigin().equalsIgnoreCase("null")) {
            user.setAddressOrigin(null);
        }

        user.setAddressDestiny(jsonObject.optString(Constants.ADDRESS_DESTINY));
        if (user.getAddressDestiny().equalsIgnoreCase("null")) {
            user.setAddressDestiny(null);
        }

        user.setNumberOfStudentsAllowed(jsonObject.optInt(Constants.STUDENTS_ALLOWED));
        if (user.getNumberOfStudentsAllowed() == '0' || user.getNumberOfStudentsAllowed() == 0) {
            user.setNumberOfStudentsAllowed(0);
        }

        user.setImage(jsonObject.optString(Constants.STUDENT_IMAGE));
        if (user.getAddressDestiny().equalsIgnoreCase("null")) {
            user.setImage(null);
        }

        user.setValueForRent(jsonObject.optDouble(Constants.VALUE_FOR_RENT));
        if (user.getValueForRent() == '0' || user.getValueForRent() == 0) {
            user.setValueForRent(0);
        }

        userDao.insert(user);
    }
}
