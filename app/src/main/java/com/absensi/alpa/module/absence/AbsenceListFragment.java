package com.absensi.alpa.module.absence;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.absensi.alpa.R;
import com.absensi.alpa.api.endpoint.attendance.AttendanceDataResponse;
import com.absensi.alpa.api.endpoint.attendance.AttendanceResponse;
import com.absensi.alpa.api.endpoint.attendance.AttendanceService;
import com.absensi.alpa.module.home.DashboardFragment;
import com.absensi.alpa.tools.Constant;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbsenceListFragment extends Fragment {

    private AbsenceListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_absent_list, container, false);

        this.adapter = new AbsenceListAdapter();
        RecyclerView rcAbsent = view.findViewById(R.id.rvAbsentList);
        rcAbsent.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));
        rcAbsent.setAdapter(adapter);

        this.setData();
        return view;
    }

    private void setData(){
        Call<AttendanceResponse> responseCall = AttendanceService.getAttendance(
                this.requireActivity(),
                Constant.URL.ATTENDANCE,
                "2020-06-01",
                "2020-07-02");

        responseCall.enqueue(new Callback<AttendanceResponse>() {
            @Override
            public void onResponse(@NotNull Call<AttendanceResponse> call, @NotNull Response<AttendanceResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        AttendanceResponse attendanceResponse = response.body();

                        if (attendanceResponse != null) {
                            adapter.getItems().clear();

                            for (AttendanceDataResponse dataResponse : attendanceResponse.getData()) {
                                AbsenceListAdapter.Item item = new AbsenceListAdapter.Item();

                                Date date1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dataResponse.getDateIn());
                                SimpleDateFormat dayNumber = new SimpleDateFormat("d", Locale.getDefault());
                                SimpleDateFormat dayInWeek = new SimpleDateFormat("E", Locale.getDefault());

                                if (date1 != null) {
                                    item.setDayNumber(dayNumber.format(date1));
                                    item.setDayWeek(dayInWeek.format(date1));
                                    item.setTimeCheckIn(dataResponse.getTimeIn());
                                    item.setTimeCheckOut(dataResponse.getTimeOut());
                                }

                                adapter.getItems().add(item);
                            }

                            if (adapter.getItems().size() > 0) {
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(AbsenceListFragment.this.getContext(), AbsenceListFragment.this.getString(R.string.error_occurred_contact_admin), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                            Toast.makeText(AbsenceListFragment.this.getContext(), jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(AbsenceListFragment.this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(AbsenceListFragment.this.getContext(), AbsenceListFragment.this.getString(R.string.error_occurred_contact_admin), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<AttendanceResponse> call, @NotNull Throwable t) {

            }
        });
    }
}
