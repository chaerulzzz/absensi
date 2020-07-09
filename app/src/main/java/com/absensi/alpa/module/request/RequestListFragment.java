package com.absensi.alpa.module.request;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.absensi.alpa.R;
import com.absensi.alpa.api.endpoint.request.RequestService;
import com.absensi.alpa.api.endpoint.request.list.RequestListDataResponse;
import com.absensi.alpa.api.endpoint.request.list.RequestListResponse;
import com.absensi.alpa.module.home.HomeActivity;
import com.absensi.alpa.module.login.LoginActivity;
import com.absensi.alpa.tools.Constant;
import com.absensi.alpa.tools.Preferences;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.absensi.alpa.tools.Tools.updateLabel;

public class RequestListFragment extends Fragment implements View.OnClickListener, RequestItemAdapter.RequestInterface {
    private MaterialButton btnSearch;
    private Calendar calendarFrom, calendarTo;
    private DatePickerDialog.OnDateSetListener dateTo, dateFrom;
    private LinearLayout llDateFrom, llDateTo;
    private TextView tvDateTo, tvDateFrom;
    private RequestItemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_list, container, false);


        this.init(view);
        this.getData();
        return view;
    }

    private void init(View view) {
        this.adapter = new RequestItemAdapter(this);
        RecyclerView rcAbsent = view.findViewById(R.id.rvRequestList);
        rcAbsent.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));
        rcAbsent.setAdapter(adapter);

        this.tvDateFrom = view.findViewById(R.id.tvDateFrom);
        this.tvDateTo = view.findViewById(R.id.tvDateTo);

        this.llDateFrom = view.findViewById(R.id.llDateFrom);
        this.llDateFrom.setOnClickListener(this);

        this.llDateTo = view.findViewById(R.id.llDateTo);
        this.llDateTo.setOnClickListener(this);

        this.btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        calendarFrom = Calendar.getInstance();
        calendarTo = Calendar.getInstance();

        dateFrom = (datePicker, i, i1, i2) -> {
            calendarFrom.set(Calendar.YEAR, i);
            calendarFrom.set(Calendar.MONTH, i1);
            calendarFrom.set(Calendar.DAY_OF_MONTH, i2);
            updateLabel(tvDateFrom, calendarFrom);
        };

        dateTo = (datePicker, i, i1, i2) -> {
            calendarTo.set(Calendar.YEAR, i);
            calendarTo.set(Calendar.MONTH, i1);
            calendarTo.set(Calendar.DAY_OF_MONTH, i2);
            updateLabel(tvDateTo, calendarTo);
        };

        updateLabel(tvDateFrom, calendarFrom);
        updateLabel(tvDateTo, calendarTo);
    }

    private void getData(){
        Call<RequestListResponse> responseCall = RequestService.getListRequest(
                this.requireActivity(),
                Constant.URL.REQUEST,
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendarFrom.getTime()),
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendarTo.getTime()));

        responseCall.enqueue(new Callback<RequestListResponse>() {
            @Override
            public void onResponse(@NotNull Call<RequestListResponse> call, @NotNull Response<RequestListResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        RequestListResponse listResponse = response.body();

                        if (listResponse != null) {
                            adapter.getItems().clear();

                            for (RequestListDataResponse dataResponse : listResponse.getData()) {
                                RequestItemAdapter.Item item = new RequestItemAdapter.Item();

                                item.setTitle(dataResponse.getRequestType());

                                String period = dataResponse.getRequestDateStart() + " - " + dataResponse.getRequestDateEnd();
                                item.setId(dataResponse.getRequestId());
                                item.setPeriod(period);
                                item.setStatus(dataResponse.getRequestStatus());
                                item.setCreated(dataResponse.getRequester());
                                adapter.getItems().add(item);
                            }

                            if (adapter.getItems().size() > 0) {
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(RequestListFragment.this.getContext(), RequestListFragment.this.getString(R.string.error_occurred_contact_admin), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                            Toast.makeText(RequestListFragment.this.getContext(), jObjError.getString("message"), Toast.LENGTH_SHORT).show();

                            if (jObjError.getString("message").equalsIgnoreCase("Unauthorized")) {
                                Preferences preferences = Preferences.getInstance();
                                preferences.begin();
                                preferences.put(Constant.CREDENTIALS.SESSION, "");
                                preferences.commit();

                                requireActivity().startActivity(new Intent(requireContext(), LoginActivity.class));
                                requireActivity().finish();
                            }
                        } catch (Exception e) {
                            Toast.makeText(RequestListFragment.this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(RequestListFragment.this.getContext(), RequestListFragment.this.getString(R.string.error_occurred_contact_admin), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<RequestListResponse> call, @NotNull Throwable t) {
                Toast.makeText(RequestListFragment.this.getContext(), RequestListFragment.this.getString(R.string.error_not_connected_to_server), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.equals(llDateFrom)) {
            new DatePickerDialog(requireActivity(), dateFrom, calendarFrom.get(Calendar.YEAR), calendarFrom.get(Calendar.MONTH), calendarFrom.get(Calendar.DAY_OF_MONTH)).show();
        } else if (v.equals(llDateTo)) {
            new DatePickerDialog(requireActivity(), dateTo, calendarTo.get(Calendar.YEAR), calendarTo.get(Calendar.MONTH), calendarTo.get(Calendar.DAY_OF_MONTH)).show();
        } else if (v.equals(btnSearch)) {
            this.getData();
        }
    }

    @Override
    public void onRequestClick(Integer position, String request_id, String type) {
        ((HomeActivity)RequestListFragment.this.requireActivity()).loadFragment(new RequestDetailFragment(type, request_id, 0));
    }
}
