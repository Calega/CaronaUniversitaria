package com.example.lucaoliveira.caronauniversitaria.ui;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.lucaoliveira.caronauniversitaria.Constants;
import com.example.lucaoliveira.caronauniversitaria.R;
import com.example.lucaoliveira.caronauniversitaria.RESTServiceApplication;
import com.example.lucaoliveira.caronauniversitaria.data.User;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServiceTask;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServicesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Lucas Calegari A. De Oliveira on 7/1/2016.
 */
public class FinishRegisterActivity extends AppCompatActivity {
    // TODO : DESCOBRIR O PORQUE, AO TIRAR FOTO, VOLTA PARA ACTIVITY ANTERIOR
    private UserEditTask mUserEditTask = null;

    private EditText mAccessType;
    private EditText mAddressOrigin;
    private EditText mAddressDestiny;
    private EditText mStudentsAllowed;

    private Button btnTakePicture;
    private ImageView thumbail;
    private ImageView imagePreview;

    private String pathFoto;
    private File foto;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_register);
        initViews();


        if (ActivityCompat.checkSelfPermission(FinishRegisterActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FinishRegisterActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        }

        controlButtonClicks();
    }

    private void controlButtonClicks() {
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDeviceSupportCamera()) {
                    Toast.makeText(getApplicationContext(),
                            "Desculpe ! Seu celular não suporta tirar fotos!",
                            Toast.LENGTH_LONG).show();
                }

                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                pathFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";//parametro representa sub pasta padrão
                foto = new File(pathFoto);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(foto));
                startActivityForResult(intentCamera, REQUEST_IMAGE_CAPTURE);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                btnTakePicture.setEnabled(true);
            } else {
                btnTakePicture.setEnabled(false);
                btnTakePicture.setText("Conceda de permissão de tirar foto :)");
            }
        }
    }

    private void initViews() {
        mStudentsAllowed = (EditText) findViewById(R.id.number_students_allowed);
        mAccessType = (EditText) findViewById(R.id.accessType);
        mAddressOrigin = (EditText) findViewById(R.id.addressOrigin);
        mAddressDestiny = (EditText) findViewById(R.id.addressDestiny);
        btnTakePicture = (Button) findViewById(R.id.btnCapturePicture);
        imagePreview = (ImageView) findViewById(R.id.imgPreview);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            imagePreview.setImageBitmap(f);
        }
    }

    /**
     * Checking device has camera hardware or not
     */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
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

    private void populateText(User user) {
        user.setAccessType(mAccessType.getText().toString());
        user.setNumberOfStudents(Integer.parseInt(mStudentsAllowed.getText().toString()));
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
            contentValues.put(Constants.STUDENTS_ALLOWED, user.getNumberOfStudents());

            ContentValues urlValues = new ContentValues();
            urlValues.put(Constants.ACCESS_TOKEN, RESTServiceApplication.getInstance().getAccessToken());

            JSONObject obj = WebServicesUtils.requestJSONObject(Constants.UPDATE_URL, WebServicesUtils.METHOD.POST, urlValues, contentValues);

            if (!hasError(obj)) {
                JSONArray jsonArray = obj.optJSONArray(Constants.INFO);
                JSONObject jsonObject = jsonArray.optJSONObject(0);
                user.setAccessType(jsonObject.optString(Constants.ACCESS_TYPE));
                user.setAddressOrigin(jsonObject.optString(Constants.ADDRESS_ORIGIN));
                user.setAddressDestiny(jsonObject.optString(Constants.ADDRESS_DESTINY));
                user.setNumberOfStudents(jsonObject.optInt(Constants.STUDENTS_ALLOWED));
                return true;
            }
            return false;
        }
    }
}
