package com.absensi.alpa.module.home;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.absensi.alpa.R;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HomeAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        this.init();
        this.setData();
    }

    private void init() {
        this.adapter = new HomeAdapter();
        this.recyclerView = findViewById(R.id.recyclerView);
    }

    private void setData() {
        if (this.adapter != null) {
            this.adapter.getItems().clear();

            HomeAdapter.Item item1 = new HomeAdapter.Item();
            item1.setText("Test");
            this.adapter.getItems().add(item1);

            HomeAdapter.Item item2 = new HomeAdapter.Item();
            item1.setText("Test");
            this.adapter.getItems().add(item2);
        }

        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        this.recyclerView.setAdapter(adapter);
    }
}
