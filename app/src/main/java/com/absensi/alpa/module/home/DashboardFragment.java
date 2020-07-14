package com.absensi.alpa.module.home;

import android.content.Intent;
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
import com.absensi.alpa.module.login.LoginActivity;
import com.absensi.alpa.tools.Constant;
import com.absensi.alpa.tools.LoadingDialog;
import com.absensi.alpa.tools.Preferences;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private TextView tvAlpha, tvTotalLeave, tvPending, tvLateIn, tvUsername;

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
        this.tvLateIn = view.findViewById(R.id.tvLateIn);
        this.tvPending = view.findViewById(R.id.tvPending);
        this.tvTotalLeave = view.findViewById(R.id.tvTotalLeave);
        this.tvUsername = view.findViewById(R.id.textView2);
    }

    private void setData() {
        LoadingDialog dialog = new LoadingDialog(requireContext());
        dialog.show();

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
                                DashboardDataResponse dataResponse = dashboardResponse.getData().get(0);

                                tvUsername.setText(dataResponse.getUserName());

                                if (dataResponse.getApproval() != null) {
                                    Preferences preferences = Preferences.getInstance();
                                    preferences.begin();
                                    preferences.put(Constant.CREDENTIALS.APPROVER, dataResponse.getApproval());
                                    preferences.commit();

                                    tvTotalLeave.setText(dataResponse.getTotalLeave());
                                } else {
                                    Preferences preferences = Preferences.getInstance();
                                    preferences.begin();
                                    preferences.put(Constant.CREDENTIALS.APPROVER, "-");
                                    preferences.commit();

                                    tvTotalLeave.setText("0");
                                }

                                if (dataResponse.getAlpha() != null) {
                                    tvAlpha.setText(dataResponse.getAlpha());
                                } else {
                                    tvAlpha.setText("0");
                                }

                                if (dataResponse.getLateIn() != null) {
                                    tvLateIn.setText(dataResponse.getLateIn());
                                } else {
                                    tvLateIn.setText("0");
                                }

                                if (dataResponse.getPending() != null) {
                                    tvPending.setText(dataResponse.getPending());
                                } else {
                                    tvPending.setText("0");
                                }

                                if (dataResponse.getTotalLeave() != null) {
                                    Preferences preferences = Preferences.getInstance();
                                    preferences.begin();
                                    preferences.put(Constant.CREDENTIALS.TOTAL_LEAVE, dataResponse.getTotalLeave());
                                    preferences.commit();

                                    tvTotalLeave.setText(dataResponse.getTotalLeave());
                                } else {
                                    Preferences preferences = Preferences.getInstance();
                                    preferences.begin();
                                    preferences.put(Constant.CREDENTIALS.TOTAL_LEAVE, "0");
                                    preferences.commit();

                                    tvTotalLeave.setText("0");
                                }

                                if (adapter != null) {
                                    adapter.getItems().clear();

                                    HomeAdapter.Item item1 = new HomeAdapter.Item();
                                    item1.setTitle(DashboardFragment.this.getString(R.string.check_in_title_dashboard_fragment));

                                    HomeAdapter.Item item2 = new HomeAdapter.Item();
                                    item2.setTitle(DashboardFragment.this.getString(R.string.check_out_title_dashboard_fragment));

                                    if (dataResponse.getTimeIn() == null || dataResponse.getTimeIn().equalsIgnoreCase("")) {
                                        item1.setTime("");
                                    } else {
                                        item1.setTime(dataResponse.getTimeIn());
                                    }

                                    if (dataResponse.getTimeOut() == null || dataResponse.getTimeOut().equalsIgnoreCase("")) {
                                        item2.setTime("");
                                    } else {
                                        item2.setTime(dataResponse.getTimeOut());
                                    }

                                    adapter.getItems().add(item1);
                                    adapter.getItems().add(item2);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            Toast.makeText(DashboardFragment.this.getContext(), DashboardFragment.this.getString(R.string.error_occurred_contact_admin), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                            Toast.makeText(DashboardFragment.this.getContext(), jObjError.getString("message"), Toast.LENGTH_SHORT).show();

                            if (jObjError.getString("message").equalsIgnoreCase("Unauthorized")) {
                                Preferences preferences = Preferences.getInstance();
                                preferences.begin();
                                preferences.put(Constant.CREDENTIALS.SESSION, "");
                                preferences.commit();

                                requireActivity().startActivity(new Intent(requireContext(), LoginActivity.class));
                                requireActivity().finish();
                            }
                        } catch (Exception e) {
                            Toast.makeText(DashboardFragment.this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(DashboardFragment.this.getContext(), DashboardFragment.this.getString(R.string.error_occurred_contact_admin), Toast.LENGTH_SHORT).show();
                } finally {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NotNull Call<DashboardResponse> call, @NotNull Throwable t) {
                t.printStackTrace();

                dialog.dismiss();
                Toast.makeText(DashboardFragment.this.getContext(), DashboardFragment.this.getString(R.string.error_not_connected_to_server), Toast.LENGTH_SHORT).show();
            }
        });
    }
}