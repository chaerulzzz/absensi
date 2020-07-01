package com.absensi.alpa.module.home;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.absensi.alpa.R;
import com.absensi.alpa.module.absence.AbsenceFragment;
import com.absensi.alpa.module.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        this.init();
        this.loadFragment(new DashboardFragment());
    }

    private void init() {
        this.bottomNavigationView = findViewById(R.id.bottom_navigation);
        this.bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    public boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_dashboard:
                fragment = new DashboardFragment();
                break;

            case R.id.navigation_absence:
                fragment = new AbsenceFragment();
                break;

            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                break;
        }

        return loadFragment(fragment);
    }
}
