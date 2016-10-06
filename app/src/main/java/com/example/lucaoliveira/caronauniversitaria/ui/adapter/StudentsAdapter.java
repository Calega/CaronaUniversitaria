package com.example.lucaoliveira.caronauniversitaria.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        public ImageView image, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            image = (ImageView) view.findViewById(R.id.image);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    public StudentsAdapter(Context mContext, List<User> albumList) {
        this.mContext = mContext;
        this.studentList = albumList;
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

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, user);
            }
        });
    }

    public static Bitmap decodeFromBase64(String image) {
        byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    /**
     * Showing popup menu_logout when tapping on 3 dots
     */
    private void showPopupMenu(View view, User student) {
        // inflate menu_logout
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_student, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(student));
        popup.show();
    }

    /**
     * Click listener for popup menu_logout items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private User user;

        public MyMenuItemClickListener(User user) {
            this.user = user;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    showStudentInformation(user);
                    return true;
                default:

            }
            return false;
        }
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
        mContext.startActivity(intent);
    }
}