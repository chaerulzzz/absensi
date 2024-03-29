package com.absensi.alpa.api.endpoint.login;

import com.absensi.alpa.api.endpoint.GeneralResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LoginResponse extends GeneralResponse implements Serializable {

    @SerializedName("data")
    @Expose
    private LoginDataResponse data;

    public LoginDataResponse getData() {
        return data;
    }

    public void setData(LoginDataResponse data) {
        this.data = data;
    }
}
