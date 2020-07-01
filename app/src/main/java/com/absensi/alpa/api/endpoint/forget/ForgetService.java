package com.absensi.alpa.api.endpoint.forget;

import android.app.Activity;

import com.absensi.alpa.api.Api;
import com.absensi.alpa.api.Parser;

import retrofit2.Call;

public class ForgetService {
    public static Call<ForgetResponse> sendForgetPassword(
            Activity activity,
            String url,
            String nip,
            String email
    ) {
        ForgetRequest request = new ForgetRequest();
        request.setEmail(email);
        request.setNip(nip);

        return Api.getRetrofit(activity)
                .create(Parser.class)
                .sendForgetPassword(url, request);
    }
}
