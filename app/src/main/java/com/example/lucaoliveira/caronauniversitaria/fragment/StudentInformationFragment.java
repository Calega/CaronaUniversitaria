package com.example.lucaoliveira.caronauniversitaria.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lucaoliveira.caronauniversitaria.R;

/**
 * Created by lucas on 21/08/2016.
 */
public class StudentInformationFragment extends Fragment {

    private ImageView thumbail;
    private TextView name, phone, email;
    private Button dismissButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_student_information, container, false);
        initVariables(view);

        return view;
    }

    private void initVariables(View view) {
        thumbail = (ImageView) view.findViewById(R.id.thumbail_student_information);
        name = (TextView) view.findViewById(R.id.student_name);
        phone = (TextView) view.findViewById(R.id.student_phone);
        email = (TextView) view.findViewById(R.id.student_email);
        dismissButton = (Button) view.findViewById(R.id.btn_dismiss);
    }


}
