package com.absensi.alpa.api.endpoint.attendance;

import android.app.Activity;

import com.absensi.alpa.api.Api;
import com.absensi.alpa.api.Parser;

import retrofit2.Call;

public class AttendanceService {
    public static Call<AttendanceResponse> getAttendance(
            Activity activity,
            String url,
            String dateStart,
            String dateEnd
    ) {
        AttendanceRequest request = new AttendanceRequest();
        request.setDateStart(dateStart);
        request.setDateEnd(dateEnd);

        return Api.getRetrofit(activity)
                .create(Parser.class)
                .getAttendance(url, request);
    }
}
