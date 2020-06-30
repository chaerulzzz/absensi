package com.absensi.alpa.api.endpoint.login;

import android.app.Activity;

import com.absensi.alpa.api.Api;
import com.absensi.alpa.api.Parser;

import retrofit2.Call;

public class LoginService {
    private static final transient String TAG = LoginService.class.getSimpleName();

    public static Call<LoginResponse> sendLogin(
            Activity activity,
            String url,
            String email,
            String password,
            String aid
    ) {
       LoginRequest loginRequest = new LoginRequest();
       loginRequest.setEmail(email);
       loginRequest.setPassword(password);
       loginRequest.setAid(aid);

        return Api.getRetrofit(activity)
                .create(Parser.class)
                .sendLogin(url, loginRequest);
    }
}
