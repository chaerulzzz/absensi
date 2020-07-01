package com.absensi.alpa.api.endpoint.dashboard;

import com.absensi.alpa.api.endpoint.GeneralResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardResponse extends GeneralResponse {

    @SerializedName("data")
    @Expose
    private DashboardDataResponse data;

    public DashboardDataResponse getData() {
        return data;
    }

    public void setData(DashboardDataResponse data) {
        this.data = data;
    }
}
