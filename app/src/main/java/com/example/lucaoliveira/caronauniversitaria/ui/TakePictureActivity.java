package com.example.lucaoliveira.caronauniversitaria.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.lucaoliveira.caronauniversitaria.R;

/**
 * Created by Lucas Calegari Alves de Oliveira on 19/09/2016.
 */
public class TakePictureActivity extends AppCompatActivity {

    private Button btnTakePicture;
    private Button btnContinue;
    private ImageView imagePreview;

    static final int REQUEST_IMAGE_CAPTURE = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        initViews();
        controlButtonClicks();
    }

    private void controlButtonClicks() {
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    private void initViews() {
        btnTakePicture = (Button) findViewById(R.id.btnCapturePicture);
        btnContinue = (Button) findViewById(R.id.btnContinue);
        imagePreview = (ImageView) findViewById(R.id.imgPreview);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imagePreview.setImageBitmap(photo);
        }
    }


}
