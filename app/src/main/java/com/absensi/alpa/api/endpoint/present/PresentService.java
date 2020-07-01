package com.absensi.alpa.api.endpoint.present;

import android.app.Activity;

import com.absensi.alpa.api.Api;
import com.absensi.alpa.api.Parser;

import retrofit2.Call;

public class PresentService {
    public static Call<PresentResponse> sendPresence(
            Activity activity,
            String url,
            String timeIn,
            String timeOut,
            String latitude,
            String longitude,
            String image
    ) {
        PresentRequest request = new PresentRequest();
        request.setTimeIn(timeIn);
        request.setTimeOut(timeOut);
        request.setLatitude(latitude);
        request.setLongitude(longitude);
        request.setImage(image);

        return Api.getRetrofit(activity)
                .create(Parser.class)
                .sendPresence(url, request);
    }
}
