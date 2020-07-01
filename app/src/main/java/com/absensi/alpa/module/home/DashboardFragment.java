package com.absensi.alpa.module.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.absensi.alpa.R;
import com.absensi.alpa.api.endpoint.dashboard.DashboardDataResponse;
import com.absensi.alpa.api.endpoint.dashboard.DashboardResponse;
import com.absensi.alpa.api.endpoint.dashboard.DashboardService;
import com.absensi.alpa.tools.Constant;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private TextView tvAlpha;

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
        this.tvAlpha = view.findViewById(R.id.tvAlpha);
    }

    private void setData() {
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        this.recyclerView.setAdapter(adapter);

        Call<DashboardResponse> responseCall = DashboardService.getDashboard(
                this.getActivity(),
                Constant.URL.DASHBOARD,
                String.valueOf(System.currentTimeMillis()));

        responseCall.enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(@NotNull Call<DashboardResponse> call, @NotNull Response<DashboardResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        DashboardResponse dashboardResponse = response.body();

                        if (dashboardResponse != null) {
                            if (dashboardResponse.getCode().equalsIgnoreCase("200")) {
                                DashboardDataResponse dataResponse = dashboardResponse.getData();

                                tvAlpha.setText(dataResponse.getAlpha());

                                if (adapter != null) {
                                    adapter.getItems().clear();

                                    HomeAdapter.Item item1 = new HomeAdapter.Item();
                                    item1.setTitle(DashboardFragment.this.getString(R.string.check_in_title_dashboard_fragment));

                                    HomeAdapter.Item item2 = new HomeAdapter.Item();
                                    item1.setTitle(DashboardFragment.this.getString(R.string.check_out_title_dashboard_fragment));

                                    if (dataResponse.getTimeIn().equalsIgnoreCase("")) {
                                        item1.setTime("");
                                    } else {
                                        item1.setTime(dataResponse.getTimeIn());
                                    }

                                    if (dataResponse.getTimeOut().equalsIgnoreCase("")) {
                                        item2.setTime("");
                                    } else {
                                        item2.setTime(dataResponse.getTimeOut());
                                    }

                                    adapter.getItems().add(item1);
                                    adapter.getItems().add(item2);
                                }
                            }
                        } else {
                            Toast.makeText(DashboardFragment.this.getContext(), DashboardFragment.this.getString(R.string.error_occurred_contact_admin), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                            Toast.makeText(DashboardFragment.this.getContext(), jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(DashboardFragment.this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(DashboardFragment.this.getContext(), DashboardFragment.this.getString(R.string.error_occurred_contact_admin), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<DashboardResponse> call, @NotNull Throwable t) {
                Toast.makeText(DashboardFragment.this.getContext(), DashboardFragment.this.getString(R.string.error_not_connected_to_server), Toast.LENGTH_SHORT).show();
            }
        });
    }
}