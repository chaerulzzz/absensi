package com.absensi.alpa.api.endpoint.dashboard;

import android.app.Activity;

import com.absensi.alpa.api.Api;
import com.absensi.alpa.api.Parser;

import retrofit2.Call;

public class DashboardService {
    public static Call<DashboardResponse> getDashboard(
            Activity activity,
            String url,
            String date
    ){
        DashboardRequest request = new DashboardRequest();
        request.setDate(date);

        return Api.getRetrofit(activity)
                .create(Parser.class)
                .getDashboard(url, request);
    }
}
