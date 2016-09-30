package com.example.lucaoliveira.caronauniversitaria.ui;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServiceTask;
import com.example.lucaoliveira.caronauniversitaria.webservices.WebServicesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lucaoliveira on 8/12/2016.
 */
public class StudentsActivity extends AppCompatActivity {
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

    private void prepareUsers() {
        int[] covers = new int[]{
                R.drawable.album1,
                R.drawable.album2,
                R.drawable.album3,
                R.drawable.album4,
                R.drawable.album5,
                R.drawable.album6,
                R.drawable.album7,
                R.drawable.album8,
                R.drawable.album9,
                R.drawable.album10,
                R.drawable.album11};

        User a = new User("Igor Artão", 2, "igor@artao.com", covers[0], "111111111", "Avenida 9 de julho", "FIAP", "RM : 66631");
        studentsList.add(a);

        a = new User("Raphael Ballico", 2, "ballico@raphael.com", covers[1], "22222222", "Avenida 10 de julho", "FIAP", "RM : 66631");
        studentsList.add(a);

        a = new User("Lucas Oliveira", 1, "lucas@oliveira.com", covers[2], "33333333", "Avenida 9 de julho", "FIAP", "RM : 66631");
        studentsList.add(a);

        a = new User("Vitor Takao", 2, "taks@vitor.com", covers[3], "44444444", "Avenida 9 de julho", "FIAP", "RM : 66631");
        studentsList.add(a);

        a = new User("Dsiaduki", 3, "igor@artao.com", covers[4], "5555555", "Avenida 9 de julho", "FIAP", "RM : 66631");
        studentsList.add(a);

        a = new User("Guilherme Coghi", 2, "coghi@guilherme.com", covers[5], "66666666", "Avenida 9 de julho", "FIAP", "RM : 66631");
        studentsList.add(a);

        a = new User("Diego Mendes", 4, "diego@mendes.com", covers[6], "77777777", "Avenida 9 de julho", "FIAP", "RM : 66631");
        studentsList.add(a);

        a = new User("Daniel Alves", 4, "daniel@alves.com", covers[7], "88888888", "Avenida 9 de julho", "FIAP", "RM : 66631");
        studentsList.add(a);

        a = new User("Ezekiel Oliveira", 2, "ezekiel@oliveira.com", covers[8], "99999999", "Avenida 9 de julho", "FIAP", "RM : 66631");
        studentsList.add(a);

        a = new User("Cesar Lino", 2, "cesar@lino.com", covers[9], "10101010", "Avenida 9 de julho", "FIAP", "RM : 66631");
        studentsList.add(a);

        adapter.notifyDataSetChanged();
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

    private void showProgress(boolean isShow) {
        findViewById(R.id.progress_retrieving_students).setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private abstract class ActivityWebServiceTask extends WebServiceTask {
        public ActivityWebServiceTask(WebServiceTask webServiceTask) {
            super(StudentsActivity.this);
        }

        @Override
        public void showProgress() {
            StudentsActivity.this.showProgress(true);
        }

        @Override
        public void hideProgress() {
            StudentsActivity.this.showProgress(false);
        }

        @Override
        public void performSuccessfulOperation() {
        }

        @Override
        protected void onPostExecute(Boolean success) {
        }
    }

    public class StudentsListTask extends ActivityWebServiceTask {
        public StudentsListTask() {
            super(mStudentsListTask);
        }

        public boolean performRequest() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.UNIVERSITY, "FIAP");
            contentValues.put(Constants.ACCESS_TYPE, "Carona");

            JSONObject object = WebServicesUtils.requestJSONObject(Constants.STUDENTS_LIST, WebServicesUtils.METHOD.POST, contentValues, true);
            Iterator<?> keys = object.keys();

            if (!hasError(object)) {
                while (keys.hasNext()) {
                    JSONArray jsonArray = object.optJSONArray(Constants.INFO);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        User user = new User();
                        user.setEmail(jsonObject.optString(Constants.EMAIL));
                        user.setName(jsonObject.optString(Constants.NAME));
                        user.setPhoneNumber(jsonObject.optString(Constants.PHONE_NUMBER));
                        user.setUniversity(jsonObject.optString(Constants.UNIVERSITY));
                        user.setAddressOrigin(jsonObject.optString(Constants.ADDRESS_ORIGIN));
                        user.setAddressDestiny(jsonObject.optString(Constants.ADDRESS_DESTINY));
                        user.setNumberOfStudentsAllowed(jsonObject.optInt(Constants.STUDENTS_ALLOWED));
                        user.setStudentRegister(jsonObject.optString(Constants.STUDENT_REGISTER));
                        prepareUsers(user);
                    }
                }
                return true;
            }

            return false;
        }
    }

    private void prepareUsers(User user) {
        studentsList.add(user);
        adapter.notifyDataSetChanged();
    }
}
