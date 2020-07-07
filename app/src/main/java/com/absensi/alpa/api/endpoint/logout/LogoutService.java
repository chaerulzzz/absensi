package com.absensi.alpa.api.endpoint.logout;

import android.app.Activity;

import com.absensi.alpa.api.Api;
import com.absensi.alpa.api.Parser;
import com.absensi.alpa.api.endpoint.GeneralResponse;

import retrofit2.Call;

public class LogoutService {

    public static Call<GeneralResponse> callLogout(
            Activity activity,
            String url
    ){
        return Api.getRetrofit(activity)
                .create(Parser.class)
                .sendLogout(url);
    }
}
