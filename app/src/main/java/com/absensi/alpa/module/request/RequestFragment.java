package com.absensi.alpa.module.request;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.absensi.alpa.api.endpoint.request.list.RequestListDataResponse;
import com.absensi.alpa.api.endpoint.request.list.RequestListResponse;
import com.absensi.alpa.api.endpoint.request.RequestService;
import com.absensi.alpa.module.home.HomeActivity;
import com.absensi.alpa.module.login.LoginActivity;
import com.absensi.alpa.tools.Constant;
import com.absensi.alpa.tools.LoadingDialog;
import com.absensi.alpa.tools.Preferences;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestFragment extends Fragment implements View.OnClickListener, RequestItemAdapter.RequestInterface {

    private RecyclerView recyclerView;
    private RequestItemAdapter adapter;
    private MaterialCardView llSick, llLeave, llPermit, llOvertime;
    private TextView tvNoData, tvViewAll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        this.init(view);
        this.setData();
        return view;
    }

    private void init(View view) {
        this.recyclerView = view.findViewById(R.id.rvRequest);
        this.adapter = new RequestItemAdapter(this);

        this.llLeave = view.findViewById(R.id.llLeave);
        this.llLeave.setOnClickListener(this);

        this.llPermit = view.findViewById(R.id.llPermit);
        this.llPermit.setOnClickListener(this);

        this.llOvertime = view.findViewById(R.id.llOvertime);
        this.llOvertime.setOnClickListener(this);

        this.llSick = view.findViewById(R.id.llSick);
        this.llSick.setOnClickListener(this);

        this.tvViewAll = view.findViewById(R.id.tvViewAll);
        this.tvViewAll.setOnClickListener(this);

        this.tvNoData = view.findViewById(R.id.noData);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void setData() {
        LoadingDialog dialog = new LoadingDialog(requireContext());
        dialog.show();

        String dateNow = new SimpleDateFormat("yyyy-MM-dd", new Locale("id", "ID")).format(new Date());
        String dateFrom1 = new SimpleDateFormat("yyyy-MM-dd", new Locale("id", "ID")).format(this.get4DaysBefore(new Date()));

        Call<RequestListResponse> responseCall = RequestService.getListRequest(
                this.requireActivity(),
                Constant.URL.REQUEST,
                dateFrom1,
                dateNow
        );

        responseCall.enqueue(new Callback<RequestListResponse>() {
            @Override
            public void onResponse(@NotNull Call<RequestListResponse> call, @NotNull Response<RequestListResponse> response) {
                try {
                    if (response.isSuccessful()) {

                        RequestListResponse listResponse = response.body();

                        if (listResponse != null) {
                            adapter.getItems().clear();

                            int x = 0;
                            for (RequestListDataResponse dataResponse : listResponse.getData()) {
                                x++;
                                RequestItemAdapter.Item item = new RequestItemAdapter.Item();

                                item.setTitle(dataResponse.getRequestType());

                                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("id", "ID")).parse(dataResponse.getRequestDateStart());
                                Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("id", "ID")).parse(dataResponse.getRequestDateEnd());
                                SimpleDateFormat time = new SimpleDateFormat("dd MMM yy", new Locale("id", "ID"));

                                if (date != null && date2 != null) {
                                    String period = time.format(date) + " - " + time.format(date2);
                                    item.setPeriod(period);
                                } else {
                                    item.setPeriod("");
                                }

                                item.setId(dataResponse.getRequestId());
                                item.setStatus(dataResponse.getRequestStatus());
                                item.setCreated(dataResponse.getRequester());
                                adapter.getItems().add(item);

                                if (x == 3) {
                                    break;
                                }
                            }

                            if (adapter.getItems().size() > 0) {
                                tvNoData.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(RequestFragment.this.getContext(), RequestFragment.this.getString(R.string.error_occurred_contact_admin), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                            Toast.makeText(RequestFragment.this.getContext(), jObjError.getString("message"), Toast.LENGTH_SHORT).show();

                            if (jObjError.getString("message").equalsIgnoreCase("Unauthorized")) {
                                Preferences preferences = Preferences.getInstance();
                                preferences.begin();
                                preferences.put(Constant.CREDENTIALS.SESSION, "");
                                preferences.commit();

                                requireActivity().startActivity(new Intent(requireContext(), LoginActivity.class));
                                requireActivity().finish();
                            }

                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } catch (Exception e) {
                            Toast.makeText(RequestFragment.this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(RequestFragment.this.getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NotNull Call<RequestListResponse> call, @NotNull Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(RequestFragment.this.getContext(), RequestFragment.this.getString(R.string.error_not_connected_to_server), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Date get4DaysBefore(Date dateNow) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateNow);

        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -4);

        return calendar.getTime();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(llLeave)) {
            ((HomeActivity)this.requireActivity()).loadFragment(new RequestCreateFragment(0));
        } else if (v.equals(llSick)) {
            ((HomeActivity)this.requireActivity()).loadFragment(new RequestCreateFragment(1));
        } else if (v.equals(llPermit)) {
            ((HomeActivity)this.requireActivity()).loadFragment(new RequestCreateFragment(2));
        } else if (v.equals(llOvertime)) {
            ((HomeActivity)this.requireActivity()).loadFragment(new RequestCreateFragment(3));
        } else if (v.equals(tvViewAll)) {
            ((HomeActivity)this.requireActivity()).loadFragment(new RequestListFragment());
        }
    }

    @Override
    public void onRequestClick(Integer position, String request_id, String type) {
        ((HomeActivity)this.requireActivity()).loadFragment(new RequestDetailFragment(type, request_id,0));
    }
}