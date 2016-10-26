package com.lucaoliveira.unicaroneiro.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lucaoliveira.unicaroneiro.R;
import com.lucaoliveira.unicaroneiro.ui.fragment.StudentListFragment;
import com.lucaoliveira.unicaroneiro.ui.fragment.NotificationsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas Calegari Alves de Oliveira on 18/10/16.
 */

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private AlertDialog alert;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.mipmap.list,
            R.mipmap.envelope
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new StudentListFragment(), "Caroneiros");
        adapter.addFrag(new NotificationsFragment(), "Notificações");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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
                Toast.makeText(MainActivity.this, ":)", Toast.LENGTH_SHORT).show();
            }
        });
        alert = builder.create();
        alert.show();
    }


    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
//            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}