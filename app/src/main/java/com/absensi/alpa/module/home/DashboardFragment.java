package com.absensi.alpa.module.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.absensi.alpa.R;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        this.init(view);
        this.setData();
        return view;
    }

    private void init(View view) {
        this.adapter = new HomeAdapter();
        this.recyclerView = view.findViewById(R.id.recyclerView);
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

        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        this.recyclerView.setAdapter(adapter);
    }
}