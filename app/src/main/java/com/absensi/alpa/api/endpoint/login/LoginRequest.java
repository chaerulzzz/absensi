package com.absensi.alpa.api.endpoint.login;

import com.absensi.alpa.api.endpoint.GeneralRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRequest extends GeneralRequest {

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

