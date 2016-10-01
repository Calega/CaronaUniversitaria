package com.example.lucaoliveira.caronauniversitaria.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.lucaoliveira.caronauniversitaria.R;

/**
 * Created by lucas on 30/06/2016.
 */
public class HomeScreenActivity extends AppCompatActivity {

//    private boolean hasPermissionToTakePicture = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

//        if (!isDeviceSupportCamera()) {
//            setPermissionToTakePicture(false);
//        } else {
//            if (ActivityCompat.checkSelfPermission(HomeScreenActivity.this, Manifest.permission.CAMERA)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(HomeScreenActivity.this, new String[]{Manifest.permission.CAMERA}, TakePictureActivity.REQUEST_IMAGE_CAPTURE);
//            }
//        }
    }


    /**
     * Checking device has camera hardware or not
     */
//    private boolean isDeviceSupportCamera() {
//        if (getApplicationContext().getPackageManager().hasSystemFeature(
//                PackageManager.FEATURE_CAMERA)) {
//            // this device has a camera
//            return true;
//        } else {
//            // no camera on this device
//            return false;
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == 0) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
//                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                setPermissionToTakePicture(false);
//            } else {
//                setPermissionToTakePicture(true);
//            }
//        }
//    }

    public void clickRegister(View view) {
        if (hasActiveInternetConnection()) {
            Intent intent = new Intent(HomeScreenActivity.this, StartRegisterActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "É necessário ter conexão com a internet!", Toast.LENGTH_SHORT).show();
        }
    }

    public void loginWithGoogle(View view) {
        Toast.makeText(getApplicationContext(), "Funcionalidade ainda não desenvolvida", Toast.LENGTH_SHORT).show();
    }

    public void clickLogin(View view) {
        if (hasActiveInternetConnection()) {
            Intent intent = new Intent(HomeScreenActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "É necessário ter conexão com a internet!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean hasActiveInternetConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

//    public boolean hasPermissionToTakePicture() {
//        return hasPermissionToTakePicture;
//    }
//
//    public void setPermissionToTakePicture(boolean hasPermission) {
//        this.hasPermissionToTakePicture = hasPermission;
//    }
}
