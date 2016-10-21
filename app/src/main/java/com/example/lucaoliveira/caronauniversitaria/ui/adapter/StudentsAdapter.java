package com.example.lucaoliveira.caronauniversitaria.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lucaoliveira.caronauniversitaria.R;
import com.example.lucaoliveira.caronauniversitaria.model.User;
import com.example.lucaoliveira.caronauniversitaria.ui.StudentInformationActivity;

import java.util.List;

/**
 * Created by lucaoliveira on 8/12/2016.
 */
public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.MyViewHolder> {

    private Context mContext;
    private List<User> studentList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }

    public StudentsAdapter(Context mContext, List<User> studentList) {
        this.mContext = mContext;
        this.studentList = studentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final User user = studentList.get(position);
        holder.title.setText(user.getName());
        holder.count.setText(user.getNumberOfStudentsAllowed() + " Estudantes Disponíveis");

        Bitmap bitmap = decodeFromBase64(user.getImage());
        holder.image.setImageBitmap(bitmap);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStudentInformation(user);
            }
        });
    }

    public static Bitmap decodeFromBase64(String image) {
        byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    private void showStudentInformation(User user) {
        Intent intent = new Intent(mContext, StudentInformationActivity.class);
        intent.putExtra(StudentInformationActivity.EXTRA_USER_THUMBNAIL, user.getImage());
        intent.putExtra(StudentInformationActivity.EXTRA_USER_NAME, user.getName());
        intent.putExtra(StudentInformationActivity.EXTRA_USER_PHONE, user.getPhoneNumber());
        intent.putExtra(StudentInformationActivity.EXTRA_USER_EMAIL, user.getEmail());
        intent.putExtra(StudentInformationActivity.EXTRA_USER_ADDRESS_ORIGIN, user.getAddressOrigin());
        intent.putExtra(StudentInformationActivity.EXTRA_USER_ADDRESS_DESTINY, user.getAddressDestiny());
        intent.putExtra(StudentInformationActivity.EXTRA_USER_REGISTER, user.getStudentRegister());
        intent.putExtra(StudentInformationActivity.EXTRA_USER_VALUE_FOR_RENT, user.getValueForRent());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
