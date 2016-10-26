package com.lucaoliveira.unicaroneiro.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.lucaoliveira.unicaroneiro.Constants;
import com.lucaoliveira.unicaroneiro.R;
import com.lucaoliveira.unicaroneiro.RESTServiceApplication;
import com.lucaoliveira.unicaroneiro.model.User;
import com.lucaoliveira.unicaroneiro.webservices.WebServiceTask;
import com.lucaoliveira.unicaroneiro.webservices.WebServicesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by Lucas Calegari A. De Oliveira on 7/1/2016.
 */
public class FinishRegisterActivity extends AppCompatActivity {
    private UserEditTask mUserEditTask = null;

    private EditText mAccessType;
    private EditText mAddressOrigin;
    private EditText mAddressDestiny;
    private EditText mStudentsAllowed;
    private EditText mValueForRent;

    private Button btnTakePicture;
    private Bitmap photoTaken;

    private ImageView imageView;
    private String uploadImage;

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_register);
        initViews();

        if (!hasCamera()) {
            btnTakePicture.setEnabled(false);
        }
    }

    // Check if user has camera
    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE
                && resultCode == RESULT_OK) {
            //get the photo
            Bundle extras = data.getExtras();
            photoTaken = (Bitmap) extras.get("data");
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(Bitmap.createScaledBitmap(photoTaken, 600, 600, false));
        }
    }

    public void finishLogin(View view) {
        mAddressOrigin.setError(null);
        mAddressDestiny.setError(null);
        mStudentsAllowed.setError(null);

        String accessType = mAccessType.getText().toString();
        String addressOrigin = mAddressOrigin.getText().toString();
        String addressDestiny = mAddressDestiny.getText().toString();
        String studentsAllowed = mStudentsAllowed.getText().toString();
        String valueForRent = mValueForRent.getText().toString();

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

        if (accessType.equals("Caroneiro") && TextUtils.isEmpty(valueForRent)) {
            mValueForRent.setError(getString(R.string.error_accessType_caroneiro_value));
            focusView = mValueForRent;
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
        btnTakePicture = (Button) findViewById(R.id.take_picture_button);
        imageView = (ImageView) findViewById(R.id.imgPreview);
        mValueForRent = (EditText) findViewById(R.id.value_for_rent);
    }

    private void populateText(User user) {
        user.setAccessType(mAccessType.getText().toString());
        if (!mStudentsAllowed.getText().toString().equals("") && mStudentsAllowed.getText().toString() != null) {
            user.setNumberOfStudentsAllowed(Integer.valueOf(mStudentsAllowed.getText().toString()));
        } else {
            user.setNumberOfStudentsAllowed(0);
        }
        if (!mValueForRent.getText().toString().equals("") && mValueForRent.getText().toString() != null) {
            user.setValueForRent(Integer.valueOf(mValueForRent.getText().toString()));
        } else {
            user.setValueForRent(0);
        }
        user.setAddressOrigin(mAddressOrigin.getText().toString());
        user.setAddressDestiny(mAddressDestiny.getText().toString());

        if (photoTaken != null) {
            uploadImage = getStringImage(photoTaken);
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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
                mValueForRent.setVisibility(View.VISIBLE);
                break;
            case R.id.radio_carona:
                if (checked)
                    mAccessType.setText("Carona");
                mStudentsAllowed.setVisibility(View.GONE);
                mValueForRent.setVisibility(View.GONE);
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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getBaseContext(), "Alguma coisa deu errado :( Tente novamente ! ", Toast.LENGTH_SHORT).show();
                FinishRegisterActivity.this.showProgress(false);
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
            contentValues.put(Constants.STUDENT_IMAGE, uploadImage);
            contentValues.put(Constants.VALUE_FOR_RENT, user.getValueForRent());

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
                user.setImage(jsonObject.optString(Constants.STUDENT_IMAGE));
                user.setValueForRent(jsonObject.optDouble(Constants.VALUE_FOR_RENT));
                return true;
            }
            return false;
        }
    }
}
