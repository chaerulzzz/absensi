package com.absensi.alpa.api;

import com.absensi.alpa.api.endpoint.attendance.AttendanceRequest;
import com.absensi.alpa.api.endpoint.attendance.AttendanceResponse;
import com.absensi.alpa.api.endpoint.dashboard.DashboardRequest;
import com.absensi.alpa.api.endpoint.dashboard.DashboardResponse;
import com.absensi.alpa.api.endpoint.forget.ForgetRequest;
import com.absensi.alpa.api.endpoint.forget.ForgetResponse;
import com.absensi.alpa.api.endpoint.login.LoginRequest;
import com.absensi.alpa.api.endpoint.login.LoginResponse;
import com.absensi.alpa.api.endpoint.present.PresentRequest;
import com.absensi.alpa.api.endpoint.present.PresentResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface Parser {

    @POST
    Call<LoginResponse> sendLogin(@Url String url, @Body LoginRequest request);

    @POST
    Call<DashboardResponse> getDashboard(@Url String url, @Body DashboardRequest request);

    @POST
    Call<ForgetResponse> sendForgetPassword(@Url String url, @Body ForgetRequest request);

    @GET
    Call<AttendanceResponse> getAttendance(@Url String url, @Body AttendanceRequest request);

    @POST
    Call<PresentResponse> sendPresence(@Url String url, @Body PresentRequest request);
}
