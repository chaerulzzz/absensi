package com.absensi.alpa.api;

import androidx.annotation.NonNull;

import com.absensi.alpa.tools.Constant;
import com.absensi.alpa.tools.Preferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        Preferences preferences = Preferences.getInstance();
        String jwtToken = preferences.getValue(Constant.CREDENTIALS.SESSION, String.class, "");
        if (jwtToken != null) {
            if (!jwtToken.isEmpty()) {
                builder.addHeader("Authorization", "Bearer " + jwtToken);
            }
        }
        return chain.proceed(builder.build());
    }
}
