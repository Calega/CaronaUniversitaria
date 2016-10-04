package com.example.lucaoliveira.caronauniversitaria.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lucaoliveira.caronauniversitaria.Constants;
import com.example.lucaoliveira.caronauniversitaria.R;
import com.example.lucaoliveira.caronauniversitaria.model.User;
import com.example.lucaoliveira.caronauniversitaria.ui.adapter.StudentsAdapter;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServicesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucaoliveira on 8/12/2016.
 */
public class StudentsActivity extends AppCompatActivity {
    public static final String TAG = StudentsActivity.class.getName();

    private StudentsListTask mStudentsListTask = null;

    private RecyclerView recyclerView;
    private StudentsAdapter adapter;
    private List<User> studentsList;

    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        studentsList = new ArrayList<>();
        adapter = new StudentsAdapter(this, studentsList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        mStudentsListTask = new StudentsListTask();
        mStudentsListTask.execute();

        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.FIAP));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_logout; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout:
                confirmLogout();
                return true;
            case R.id.change_email:
                openChangeEmailFragment();
                return true;
            case R.id.change_password:
                openChangePasswordFragment();
                return true;
            case R.id.change_register:
                openChangeRegisterFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openChangeRegisterFragment() {
        Intent intent = new Intent(getBaseContext(), UpdateRegisterActivity.class);
        startActivity(intent);
    }

    private void openChangePasswordFragment() {
        Intent intent = new Intent(getBaseContext(), UpdatePasswordActivity.class);
        startActivity(intent);
    }

    private void openChangeEmailFragment() {
        Intent intent = new Intent(getBaseContext(), UpdateEmailActivity.class);
        startActivity(intent);
    }

    private void confirmLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.action_logout));
        builder.setMessage(getResources().getString(R.string.action_confirm_logout));
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent mIntent = new Intent(getBaseContext(), HomeScreenActivity.class);
                startActivity(mIntent);
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(StudentsActivity.this, ":)", Toast.LENGTH_SHORT).show();
            }
        });
        alert = builder.create();
        alert.show();
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
            mMessage = mContext.getString(R.string.error_url_not_found);
            return true;
        }

    }
}
