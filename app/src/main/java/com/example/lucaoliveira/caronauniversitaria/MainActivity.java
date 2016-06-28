package com.example.lucaoliveira.caronauniversitaria;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.lucaoliveira.caronauniversitaria.data.User;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServiceTask;

public class MainActivity extends AppCompatActivity {
    private UserInfoTask mUserInfoTask = null;
    private UserEditTask mUserEditTask = null;
    private UserResetTask mUserResetTask = null;
    private UserDeleteTask mUserDeleteTask = null;

    private EditText mEmailText;
    private EditText mPasswordText;
    private EditText mNameText;
    private EditText mPhoneNumberText;
    private EditText mNoteText;

    private interface ConfirmationListener {
        void onConfirmation(boolean isConfirmed);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        showProgress(true);
        mUserInfoTask = new UserInfoTask();
        mUserInfoTask.execute();
    }

    private void showProgress(final boolean isShow) {
        findViewById(R.id.progress).setVisibility(isShow ? View.VISIBLE : View.GONE);
        findViewById(R.id.info_form).setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void initViews() {
        mEmailText = (EditText) findViewById(R.id.email);
        mPasswordText = (EditText) findViewById(R.id.password);
        mNameText = (EditText) findViewById(R.id.name);
        mPhoneNumberText = (EditText) findViewById(R.id.phonenumber);
        mNoteText = (EditText) findViewById(R.id.note);
    }

    private void populateText() {
        User user = RESTServiceApplication.getInstance().getUser();
        mEmailText.setText(user.getEmail());
        mPasswordText.setText(user.getPassword());
        mNameText.setText(user.getName() == null ? "" : user.getName());
        mPhoneNumberText.setText(user.getPhoneNumber() == null ? "" : user.getPhoneNumber());
        mNoteText.setText(user.getNote() == null ? "" : user.getNote());
    }

    private abstract class ActivityWebServiceTask extends WebServiceTask {
        public ActivityWebServiceTask(WebServiceTask webServiceTask) {
            super(MainActivity.this);
        }

        @Override
        public void showProgress() {
            MainActivity.this.showProgress(true);
        }

        @Override
        public void hideProgress() {
            MainActivity.this.showProgress(false);
        }

        @Override
        public void performSuccessfulOperation() {
            populateText();
        }
    }

    public class UserInfoTask extends ActivityWebServiceTask {
        public UserInfoTask() {
            super(mUserInfoTask);
        }

        public boolean performRequest() {
            return true;
        }
    }

    public class UserEditTask extends ActivityWebServiceTask {
        public UserEditTask() {
            super(mUserEditTask);
        }

        public boolean performRequest() {
            return true;
        }
    }

    public class UserResetTask extends ActivityWebServiceTask {
        public UserResetTask() {
            super(mUserResetTask);
        }

        public boolean performRequest() {
            return true;
        }
    }

    public class UserDeleteTask extends ActivityWebServiceTask {
        public UserDeleteTask() {
            super(mUserDeleteTask);
        }

        public boolean performRequest() {
            return true;
        }
    }

}
