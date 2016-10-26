package com.lucaoliveira.unicaroneiro.ui.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lucaoliveira.unicaroneiro.Constants;
import com.lucaoliveira.unicaroneiro.R;
import com.lucaoliveira.unicaroneiro.model.User;
import com.lucaoliveira.unicaroneiro.ui.adapter.StudentsAdapter;
import com.lucaoliveira.unicaroneiro.webservices.WebServicesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas Calegari Alves de Oliveira on 18/10/16.
 */

public class StudentListFragment extends Fragment {
    public static final String TAG = StudentListFragment.class.getName();

    private StudentsListTask mStudentsListTask = null;

    private RecyclerView recyclerView;
    private StudentsAdapter adapter;
    private List<User> studentsList;

    public StudentListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        studentsList = new ArrayList<>();
        adapter = new StudentsAdapter(getActivity().getApplicationContext(), studentsList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        Toast.makeText(getActivity().getApplicationContext(), "Aguarde um momento...", Toast.LENGTH_SHORT).show();

        mStudentsListTask = new StudentsListTask();
        mStudentsListTask.execute();

        return view;
    }

    private class StudentsListTask extends AsyncTask<Void, JSONObject, Void> {
        private String mMessage;
        private Context mContext;

        @Override
        protected Void doInBackground(Void... params) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.UNIVERSITY, "FIAP");

            JSONObject object = WebServicesUtils.requestJSONObject(Constants.STUDENTS_LIST, WebServicesUtils.METHOD.POST, contentValues, true);

            if (!hasError(object)) {
                JSONArray jsonArray = object.optJSONArray(Constants.INFO);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    publishProgress(jsonObject);
                }
                return null;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONObject... values) {
            User user = new User();
            user.setEmail(values[0].optString(Constants.EMAIL));
            user.setName(values[0].optString(Constants.NAME));
            user.setPhoneNumber(values[0].optString(Constants.PHONE_NUMBER));
            user.setUniversity(values[0].optString(Constants.UNIVERSITY));
            user.setAddressOrigin(values[0].optString(Constants.ADDRESS_ORIGIN));
            user.setAddressDestiny(values[0].optString(Constants.ADDRESS_DESTINY));
            user.setNumberOfStudentsAllowed(values[0].optInt(Constants.STUDENTS_ALLOWED));
            user.setStudentRegister(values[0].optString(Constants.STUDENT_REGISTER));
            user.setValueForRent(values[0].optDouble(Constants.VALUE_FOR_RENT));
            user.setImage(values[0].optString(Constants.STUDENT_IMAGE));
            studentsList.add(user);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
        }

        public boolean hasError(JSONObject obj) {
            if (obj != null) {
                int status = obj.optInt(Constants.STATUS);
                Log.d(TAG, "Response " + obj.toString());
                mMessage = obj.optString(Constants.MESSAGE);

                if (status == Constants.STATUS_ERROR || status == Constants.STATUS_UNAUTHORIZED) {
                    return true;
                } else {
                    return false;
                }
            }
            mMessage = getResources().getString(R.string.error_url_not_found);
            return true;
        }
    }
}
