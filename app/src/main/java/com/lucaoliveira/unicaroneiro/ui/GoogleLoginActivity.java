package com.lucaoliveira.unicaroneiro.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lucaoliveira.unicaroneiro.Constants;
import com.lucaoliveira.unicaroneiro.R;
import com.lucaoliveira.unicaroneiro.dao.UserDao;
import com.lucaoliveira.unicaroneiro.model.User;
import com.lucaoliveira.unicaroneiro.webservices.WebServiceTask;
import com.lucaoliveira.unicaroneiro.webservices.WebServicesUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Lucas Calegari A. De Oliveira on 20/10/2016.
 */
public class GoogleLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private EmailLoginTask mEmailTask = null;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton mSignInButton;

    private static final int RC_SIGN_IN = 9001;

    private String googleEmail;
    private String googleName;

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);

        userDao = new UserDao(getBaseContext());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /*fragment activity */, this /* on connection failure */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mSignInButton = (SignInButton) findViewById(R.id.login_with_google);
        mSignInButton.setSize(SignInButton.SIZE_WIDE);
        mSignInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_with_google:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("GoogleLoginActivity", "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            googleEmail = acct.getEmail();
            googleName = acct.getDisplayName();

            mEmailTask = new EmailLoginTask();
            mEmailTask.execute();
        }
    }

    private abstract class ActivityWebServiceTask extends WebServiceTask {
        public ActivityWebServiceTask(WebServiceTask webServiceTask) {
            super(GoogleLoginActivity.this);
        }

        @Override
        public void showProgress() {
        }

        @Override
        public void hideProgress() {
        }

        @Override
        public void performSuccessfulOperation() {
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Intent intent = new Intent(GoogleLoginActivity.this, StartRegisterActivity.class);
                intent.putExtra(StartRegisterActivity.EXTRA_USER_EMAIL, googleEmail);
                intent.putExtra(StartRegisterActivity.EXTRA_USER_NAME, googleName);
                startActivity(intent);
            } else {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.prompt_welcome_back), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }

    public class EmailLoginTask extends ActivityWebServiceTask {

        public EmailLoginTask() {
            super(mEmailTask);
        }

        public boolean performRequest() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.EMAIL, googleEmail);

            JSONObject object = WebServicesUtils.requestJSONObject(Constants.SOCIAL_NETWORK_LOGIN, WebServicesUtils.METHOD.POST, contentValues, true);

            if (!hasError(object)) {
                persistUser(object);
                return true;
            }

            return false;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("GoogleLoginActivity", "onConnectionFailed:" + connectionResult);
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
