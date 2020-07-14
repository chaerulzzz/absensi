package com.absensi.alpa.module.absence;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.absensi.alpa.module.login.LoginActivity;
import com.absensi.alpa.tools.Constant;
import com.absensi.alpa.tools.LoadingDialog;
import com.absensi.alpa.tools.Preferences;
import com.google.android.material.button.MaterialButton;

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

import static com.absensi.alpa.tools.Tools.updateLabel;

public class AbsenceListFragment extends Fragment implements View.OnClickListener {

    private MaterialButton btnSearch;
    private Calendar calendarFrom, calendarTo;
    private DatePickerDialog.OnDateSetListener dateTo, dateFrom;
    private LinearLayout llDateFrom, llDateTo;
    private TextView tvDateTo, tvDateFrom;
    private AbsenceListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_absent_list, container, false);


        this.init(view);
        this.getData();
        return view;
    }

    private void init(View view) {
        this.adapter = new AbsenceListAdapter();
        RecyclerView rcAbsent = view.findViewById(R.id.rvAbsentList);
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
        LoadingDialog dialog = new LoadingDialog(requireContext());
        dialog.show();

        Call<AttendanceResponse> responseCall = AttendanceService.getAttendance(
                this.requireActivity(),
                Constant.URL.ATTENDANCE_LIST,
                new SimpleDateFormat("yyyy-MM-dd", new Locale("id", "ID")).format(calendarFrom.getTime()),
                new SimpleDateFormat("yyyy-MM-dd", new Locale("id", "ID")).format(calendarTo.getTime()));

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

                                Date date1 = new SimpleDateFormat("yyyy-MM-dd", new Locale("id", "ID")).parse(dataResponse.getDateIn());

                                Date date2 = null;
                                Date date3 = null;

                                if (dataResponse.getTimeIn() != null) {
                                    date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("id", "ID")).parse(dataResponse.getTimeIn());
                                }

                                if (dataResponse.getTimeOut() != null) {
                                    date3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("id", "ID")).parse(dataResponse.getTimeOut());
                                }
                                SimpleDateFormat dayNumber = new SimpleDateFormat("d", new Locale("id", "ID"));
                                SimpleDateFormat dayInWeek = new SimpleDateFormat("E", new Locale("id", "ID"));
                                SimpleDateFormat time = new SimpleDateFormat("HH:mm", new Locale("id", "ID"));

                                if (date1 != null) {
                                    item.setDayNumber(dayNumber.format(date1));
                                    item.setDayWeek(dayInWeek.format(date1));

                                    if (date2 != null) {
                                        item.setTimeCheckIn(time.format(date2));
                                    } else {
                                        item.setTimeCheckIn("");
                                    }

                                    if (date3 != null) {
                                        item.setTimeCheckOut(time.format(date3));
                                    } else {
                                        item.setTimeCheckOut("");
                                    }
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

                            if (jObjError.getString("message").equalsIgnoreCase("Unauthorized")) {
                                Preferences preferences = Preferences.getInstance();
                                preferences.begin();
                                preferences.put(Constant.CREDENTIALS.SESSION, "");
                                preferences.commit();

                                requireActivity().startActivity(new Intent(requireContext(), LoginActivity.class));
                                requireActivity().finish();
                            }
                        } catch (Exception e) {
                            Toast.makeText(AbsenceListFragment.this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(AbsenceListFragment.this.getContext(), AbsenceListFragment.this.getString(R.string.error_occurred_contact_admin), Toast.LENGTH_SHORT).show();
                } finally {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NotNull Call<AttendanceResponse> call, @NotNull Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(AbsenceListFragment.this.getContext(), AbsenceListFragment.this.getString(R.string.error_not_connected_to_server), Toast.LENGTH_SHORT).show();
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
}
