package com.absensi.alpa.api.endpoint.login;

import com.absensi.alpa.api.endpoint.GeneralRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LoginResponse extends GeneralRequest implements Serializable {

    @SerializedName("data")
    @Expose
    private List<LoginDataResponse> data;

    public List<LoginDataResponse> getData() {
        return data;
    }

    public void setData(List<LoginDataResponse> data) {
        this.data = data;
    }
}
