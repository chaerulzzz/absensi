package com.absensi.alpa.module.approval;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.absensi.alpa.R;
import com.absensi.alpa.api.endpoint.approval.ApprovalService;
import com.absensi.alpa.api.endpoint.approval.list.ApprovalListDataResponse;
import com.absensi.alpa.api.endpoint.approval.list.ApprovalListResponse;
import com.absensi.alpa.entity.ApprovalEntity;
import com.absensi.alpa.module.home.HomeActivity;
import com.absensi.alpa.module.login.LoginActivity;
import com.absensi.alpa.module.request.RequestDetailFragment;
import com.absensi.alpa.module.request.RequestItemAdapter;
import com.absensi.alpa.tools.Constant;
import com.absensi.alpa.tools.Preferences;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApprovalFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, RequestItemAdapter.RequestInterface {

    private MaterialCardView cvPending, cvLeave, cvPermit, cvSick, cvOvertime;
    private TextView tvPending, tvLeave, tvPermit, tvSick, tvOvertime;
    private RecyclerView rvApproval;
    private RequestItemAdapter adapter;
    private Realm realm;
    private AppCompatSpinner spnName, spnStatus;
    private ArrayAdapter<String> nameAdapter;
    private List<String> lstNameFilter, lstStatusFilter;
    private ArrayAdapter<String> statusAdapter;
    private String nameFilter, statusFilter;
    private int type;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_approval, container, false);

        this.init(view);
        this.getData();
        return view;
    }

    private void init(View view){
        this.realm = Realm.getDefaultInstance();
        this.cvPending = view.findViewById(R.id.cvPending);
        this.cvPending.setOnClickListener(this);

        this.lstNameFilter = new ArrayList<>();
        this.lstStatusFilter = new ArrayList<>();

        this.cvLeave = view.findViewById(R.id.cvLeave);
        this.cvLeave.setOnClickListener(this);

        this.cvPermit = view.findViewById(R.id.cvPermit);
        this.cvPermit.setOnClickListener(this);

        this.cvSick = view.findViewById(R.id.cvSick);
        this.cvSick.setOnClickListener(this);

        this.cvOvertime = view.findViewById(R.id.cvOvertime);
        this.cvOvertime.setOnClickListener(this);

        this.adapter = new RequestItemAdapter(this);
        this.rvApproval = view.findViewById(R.id.rvApproval);
        this.rvApproval.setLayoutManager(new LinearLayoutManager(this.requireContext(), RecyclerView.VERTICAL, false));
        this.rvApproval.setAdapter(adapter);

        this.tvLeave = view.findViewById(R.id.tvLeave);
        this.tvPending = view.findViewById(R.id.tvPending);
        this.tvSick = view.findViewById(R.id.tvSick);
        this.tvPermit = view.findViewById(R.id.tvPermit);
        this.tvOvertime = view.findViewById(R.id.tvOvertime);

        spnName = view.findViewById(R.id.approval_from);
        nameAdapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_row, lstNameFilter);
        spnName.setAdapter(nameAdapter);
        spnName.setOnItemSelectedListener(this);

        spnStatus = view.findViewById(R.id.approval_status);
        statusAdapter = new ArrayAdapter<>(requireContext(),R.layout.spinner_row, lstStatusFilter);
        spnStatus.setAdapter(statusAdapter);
        spnStatus.setOnItemSelectedListener(this);
    }

    private void getData() {
        type = 0;
        Call<ApprovalListResponse> responseCall = ApprovalService.getApprovalList(requireActivity(), Constant.URL.APPROVAL);

        responseCall.enqueue(new Callback<ApprovalListResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApprovalListResponse> call, @NotNull Response<ApprovalListResponse> response) {
                if (response.isSuccessful()) {
                    ApprovalListResponse listResponse = response.body();

                    if (listResponse != null) {
                        lstNameFilter.clear();
                        lstStatusFilter.clear();

                        lstNameFilter.add("Semua");
                        lstStatusFilter.add("Semua");

                        realm.executeTransaction(realm1 -> {
                            realm1.delete(ApprovalEntity.class);

                            for(ApprovalListDataResponse dataResponse: listResponse.getData()){
                                ApprovalEntity entity = new ApprovalEntity();
                                entity.setId(Long.parseLong(dataResponse.getId()));
                                entity.setDateStart(dataResponse.getDateStart());
                                entity.setDateEnd(dataResponse.getDateEnd());
                                entity.setStatus(dataResponse.getStatus());
                                entity.setType(dataResponse.getType());
                                entity.setRequester(dataResponse.getRequester());

                                realm1.copyToRealmOrUpdate(entity);
                            }
                        });

                        for (ApprovalListDataResponse detail: listResponse.getData()) {

                            if (!lstNameFilter.contains(detail.getRequester())){
                                lstNameFilter.add(detail.getRequester());
                            }

                            if (!lstStatusFilter.contains(detail.getStatus())) {
                                lstStatusFilter.add(detail.getStatus());
                            }
                        }

                        nameAdapter.notifyDataSetChanged();
                        statusAdapter.notifyDataSetChanged();

                        if (type == 0) {
                            setTabColor(tvPending);
                            prepareData("Awaiting");
                        } else if (type == 1) {
                            setTabColor(tvLeave);
                            prepareData("Leave Request");
                        } else if (type == 2) {
                            setTabColor(tvPermit);
                            prepareData("Permit Request");
                        } else if (type == 3) {
                            setTabColor(tvSick);
                            prepareData("Sick Request");
                        } else if (type == 4) {
                            setTabColor(tvOvertime);
                            prepareData("Overtime Request");
                        }
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                        Toast.makeText(ApprovalFragment.this.getContext(), jObjError.getString("message"), Toast.LENGTH_SHORT).show();

                        if (jObjError.getString("message").equalsIgnoreCase("Unauthorized")) {
                            Preferences preferences = Preferences.getInstance();
                            preferences.begin();
                            preferences.put(Constant.CREDENTIALS.SESSION, "");
                            preferences.commit();

                            requireActivity().startActivity(new Intent(requireContext(), LoginActivity.class));
                            requireActivity().finish();
                        }
                    } catch (Exception e) {
                        Toast.makeText(ApprovalFragment.this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ApprovalListResponse> call, @NotNull Throwable t) {
                Toast.makeText(ApprovalFragment.this.getContext(), ApprovalFragment.this.getString(R.string.error_not_connected_to_server), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.equals(cvPending)) {
            setTabColor(tvPending);
            prepareData("Awaiting");
            this.type = 0;
        } else if (view.equals(cvLeave)) {
            setTabColor(tvLeave);
            prepareData("Leave Request");
            this.type = 1;
        } else if (view.equals(cvPermit)) {
            setTabColor(tvPermit);
            prepareData("Permit Request");
            this.type = 2;
        } else if (view.equals(cvSick)) {
            setTabColor(tvSick);
            prepareData("Sick Request");
            this.type = 3;
        } else if (view.equals(cvOvertime)) {
            setTabColor(tvOvertime);
            prepareData("Overtime Request");
            this.type = 4;
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (this.realm != null) {
            this.realm = null;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.equals(spnName)) {
            nameFilter = spnName.getSelectedItem().toString();
        } else if (parent.equals(spnStatus)){
            statusFilter = spnStatus.getSelectedItem().toString();
        }

        if (type == 0) {
            prepareData("Awaiting");
        } else if (type == 1) {
            prepareData("Leave Request");
        } else if (type == 2) {
            prepareData("Permit Request");
        } else if (type == 3) {
            prepareData("Sick Request");
        } else if (type == 4) {
            prepareData("Overtime Request");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void prepareData(String condition) {
        realm = Realm.getDefaultInstance();

        try {
            if (realm.isInTransaction()) {
                realm.close();
            }

            if (realm.isClosed()) {
                realm = Realm.getDefaultInstance();
            }

            RealmResults<ApprovalEntity> results;
            RealmQuery<ApprovalEntity> query = realm.where(ApprovalEntity.class);

            if (nameFilter != null && nameFilter.length() > 0) {
                if (!nameFilter.equals("Semua")) {
                    query.equalTo("requester", nameFilter);
                }
            }

            if (statusFilter != null && statusFilter.length() > 0) {
                if (!statusFilter.equals("Semua")) {
                    query.equalTo("status", statusFilter);
                }
            }

            if (condition.length() > 8) {
                query.equalTo("type", condition).findAll();
            } else {
                query.equalTo("status", condition).findAll();
            }

            results = query.findAll();

            if (results != null) {
                ArrayList<HashMap<String, Object>> mAllData = new ArrayList<>();

                if (results.size() > 0) {
                    adapter.getItems().clear();
                    for (ApprovalEntity entity : results) {
                        if (entity != null) {
                            RequestItemAdapter.Item item = new RequestItemAdapter.Item();

                            item.setTitle(entity.getType());

                            String period = entity.getDateStart() + " - " + entity.getDateEnd();
                            item.setId(entity.getId().toString());
                            item.setPeriod(period);
                            item.setStatus(entity.getStatus());
                            item.setCreated(entity.getRequester());
                            adapter.getItems().add(item);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.getItems().clear();
                    adapter.notifyDataSetChanged();
                }
            } else {
                adapter.getItems().clear();
                adapter.notifyDataSetChanged();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            realm.close();
        }
    }

    private void setTabColor(TextView textView) {
        if (textView.equals(tvPending)) {
            textView.setTextColor(getResources().getColor(android.R.color.white));
            tvPermit.setTextColor(getResources().getColor(android.R.color.black));
            tvLeave.setTextColor(getResources().getColor(android.R.color.black));
            tvSick.setTextColor(getResources().getColor(android.R.color.black));
            tvOvertime.setTextColor(getResources().getColor(android.R.color.black));
            cvPending.setCardBackgroundColor(getResources().getColor(R.color.leaveColorBackground));
            cvPermit.setCardBackgroundColor(getResources().getColor(android.R.color.white));
            cvLeave.setCardBackgroundColor(getResources().getColor(android.R.color.white));
            cvSick.setCardBackgroundColor(getResources().getColor(android.R.color.white));
            cvOvertime.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        } else if (textView.equals(tvLeave)) {
            textView.setTextColor(getResources().getColor(android.R.color.white));
            tvPermit.setTextColor(getResources().getColor(android.R.color.black));
            tvPending.setTextColor(getResources().getColor(android.R.color.black));
            tvSick.setTextColor(getResources().getColor(android.R.color.black));
            tvOvertime.setTextColor(getResources().getColor(android.R.color.black));
            cvLeave.setCardBackgroundColor(getResources().getColor(R.color.leaveColorBackground));
            cvPermit.setCardBackgroundColor(getResources().getColor(android.R.color.white));
            cvPending.setCardBackgroundColor(getResources().getColor(android.R.color.white));
            cvSick.setCardBackgroundColor(getResources().getColor(android.R.color.white));
            cvOvertime.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        } else if (textView.equals(tvPermit)) {
            textView.setTextColor(getResources().getColor(android.R.color.white));
            tvPending.setTextColor(getResources().getColor(android.R.color.black));
            tvLeave.setTextColor(getResources().getColor(android.R.color.black));
            tvSick.setTextColor(getResources().getColor(android.R.color.black));
            tvOvertime.setTextColor(getResources().getColor(android.R.color.black));
            cvPermit.setCardBackgroundColor(getResources().getColor(R.color.leaveColorBackground));
            cvPending.setCardBackgroundColor(getResources().getColor(android.R.color.white));
            cvLeave.setCardBackgroundColor(getResources().getColor(android.R.color.white));
            cvSick.setCardBackgroundColor(getResources().getColor(android.R.color.white));
            cvOvertime.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        } else if (textView.equals(tvSick)) {
            textView.setTextColor(getResources().getColor(android.R.color.white));
            tvPermit.setTextColor(getResources().getColor(android.R.color.black));
            tvLeave.setTextColor(getResources().getColor(android.R.color.black));
            tvPending.setTextColor(getResources().getColor(android.R.color.black));
            tvOvertime.setTextColor(getResources().getColor(android.R.color.black));
            cvSick.setCardBackgroundColor(getResources().getColor(R.color.leaveColorBackground));
            cvPermit.setCardBackgroundColor(getResources().getColor(android.R.color.white));
            cvLeave.setCardBackgroundColor(getResources().getColor(android.R.color.white));
            cvPending.setCardBackgroundColor(getResources().getColor(android.R.color.white));
            cvOvertime.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        } else if (textView.equals(tvOvertime)) {
            textView.setTextColor(getResources().getColor(android.R.color.white));
            tvPermit.setTextColor(getResources().getColor(android.R.color.black));
            tvLeave.setTextColor(getResources().getColor(android.R.color.black));
            tvSick.setTextColor(getResources().getColor(android.R.color.black));
            tvPending.setTextColor(getResources().getColor(android.R.color.black));
            cvOvertime.setCardBackgroundColor(getResources().getColor(R.color.leaveColorBackground));
            cvPermit.setCardBackgroundColor(getResources().getColor(android.R.color.white));
            cvLeave.setCardBackgroundColor(getResources().getColor(android.R.color.white));
            cvSick.setCardBackgroundColor(getResources().getColor(android.R.color.white));
            cvPending.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        }
    }

    @Override
    public void onRequestClick(Integer position, String request_id, String type) {
        ((HomeActivity)this.requireActivity()).loadFragment(new RequestDetailFragment(type, request_id,1));
    }
}
