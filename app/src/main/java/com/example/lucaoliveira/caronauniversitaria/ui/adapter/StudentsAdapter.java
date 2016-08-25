package com.example.lucaoliveira.caronauniversitaria.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lucaoliveira.caronauniversitaria.R;
import com.example.lucaoliveira.caronauniversitaria.data.User;
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
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
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
        final User student = studentList.get(position);
        Log.d("StudentsAdapter", "onBindViewHolder Student >>>> " + student.getPhoneNumber());
        Log.d("StudentsAdapter", "onBindViewHolder Student >>>> " + student.getEmail());
        Log.d("StudentsAdapter", "onBindViewHolder Student >>>> " + student.getAddressOrigin());
        Log.d("StudentsAdapter", "onBindViewHolder Student >>>> " + student.getAddressDestiny());
        Log.d("StudentsAdapter", "onBindViewHolder Student >>>> " + student.getName());
        holder.title.setText(student.getName());
        holder.count.setText(student.getNumberOfStudents() + " Estudantes Disponíveis");

        Glide.with(mContext).load(student.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, student);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, User student) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_student, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(student));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private User student;

        public MyMenuItemClickListener(User student) {
            this.student = student;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Log.d("StudentsAdapter", "MyMenuItemClickListener Student >>>> " + student.getPhoneNumber());
                    Log.d("StudentsAdapter", "MyMenuItemClickListener Student >>>> " + student.getEmail());
                    Log.d("StudentsAdapter", "MyMenuItemClickListener Student >>>> " + student.getAddressOrigin());
                    Log.d("StudentsAdapter", "MyMenuItemClickListener Student >>>> " + student.getAddressDestiny());
                    Log.d("StudentsAdapter", "MyMenuItemClickListener Student >>>> " + student.getName());
                    Intent intent = new Intent(mContext, StudentInformationActivity.class);
//                    intent.putExtra(StudentInformationActivity.EXTRA_USER_THUMBNAIL, student.getThumbnail());
                    intent.putExtra(StudentInformationActivity.EXTRA_USER_NAME, student.getName());
                    intent.putExtra(StudentInformationActivity.EXTRA_USER_PHONE, student.getPhoneNumber());
                    intent.putExtra(StudentInformationActivity.EXTRA_USER_EMAIL, student.getEmail());
                    intent.putExtra(StudentInformationActivity.EXTRA_USER_ADDRESS_ORIGIN, student.getAddressOrigin());
                    intent.putExtra(StudentInformationActivity.EXTRA_USER_ADDRESS_DESTINY, student.getAddressDestiny());
                    mContext.startActivity(intent);
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
}
