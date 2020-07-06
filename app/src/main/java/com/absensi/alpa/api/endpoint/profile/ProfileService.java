package com.absensi.alpa.api.endpoint.profile;

import android.app.Activity;

import com.absensi.alpa.api.Api;
import com.absensi.alpa.api.Parser;

import retrofit2.Call;

public class ProfileService {

    public static Call<ProfileResponse> getProfile(
            Activity activity,
            String url
    ) {
        return Api.getRetrofit(activity)
                .create(Parser.class)
                .getProfile(url);
    }

    public static Call<ProfileEditResponse> sendProfile(
            Activity activity,
            String url,
            String birthPlace,
            String birthDate,
            String password,
            String newPassword
    ) {
        ProfileEditRequest request = new ProfileEditRequest();
        request.setBirthDate(birthDate);
        request.setBirthPlace(birthPlace);
        request.setPassword(password);
        request.setPasswordNew(newPassword);

        return Api.getRetrofit(activity)
                .create(Parser.class)
                .sendProfile(url, request);
    }
}
