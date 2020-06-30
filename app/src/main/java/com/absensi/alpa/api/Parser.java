package com.absensi.alpa.api;

import com.absensi.alpa.api.endpoint.login.LoginRequest;
import com.absensi.alpa.api.endpoint.login.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface Parser {

    @POST
    Call<LoginResponse> sendLogin(@Url String url, @Body LoginRequest request);

}
