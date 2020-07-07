package com.absensi.alpa.api;

import com.absensi.alpa.api.endpoint.GeneralResponse;
import com.absensi.alpa.api.endpoint.approval.list.ApprovalListResponse;
import com.absensi.alpa.api.endpoint.approval.process.ApprovalProcessRequest;
import com.absensi.alpa.api.endpoint.approval.process.ApprovalProcessResponse;
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
import com.absensi.alpa.api.endpoint.profile.ProfileEditRequest;
import com.absensi.alpa.api.endpoint.profile.ProfileEditResponse;
import com.absensi.alpa.api.endpoint.profile.ProfileResponse;
import com.absensi.alpa.api.endpoint.request.detail.RequestDetailResponse;
import com.absensi.alpa.api.endpoint.request.insert.RequestInsertRequest;
import com.absensi.alpa.api.endpoint.request.insert.RequestInsertResponse;
import com.absensi.alpa.api.endpoint.request.list.RequestListRequest;
import com.absensi.alpa.api.endpoint.request.list.RequestListResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Parser {

    @POST
    Call<LoginResponse> sendLogin(@Url String url, @Body LoginRequest request);

    @POST
    Call<DashboardResponse> getDashboard(@Url String url, @Body DashboardRequest request);

    @POST
    Call<ForgetResponse> sendForgetPassword(@Url String url, @Body ForgetRequest request);

    @POST
    Call<AttendanceResponse> getAttendance(@Url String url, @Body AttendanceRequest request);

    @POST
    Call<PresentResponse> sendPresence(@Url String url, @Body PresentRequest request);

    @POST
    Call<RequestListResponse> getRequestList(@Url String url, @Body RequestListRequest request);

    @POST
    Call<RequestInsertResponse> sendRequest(@Url String url, @Body RequestInsertRequest request);

    @GET
    Call<RequestDetailResponse> getDetail(@Url String url, @Query("id") String id);

    @GET
    Call<ApprovalListResponse> getList(@Url String url);

    @PUT
    Call<ApprovalProcessResponse> sendApproval(@Url String url, @Body ApprovalProcessRequest request);

    @GET
    Call<ProfileResponse> getProfile(@Url String url);

    @PUT
    Call<ProfileEditResponse> sendProfile(@Url String url, @Body ProfileEditRequest request);

    @POST
    Call<GeneralResponse> sendLogout(@Url String url);
}
