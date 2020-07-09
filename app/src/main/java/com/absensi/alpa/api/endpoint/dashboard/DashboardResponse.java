package com.absensi.alpa.api.endpoint.dashboard;

import com.absensi.alpa.api.endpoint.GeneralResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DashboardResponse extends GeneralResponse {

    @SerializedName("data")
    @Expose
    private List<DashboardDataResponse> data;

    public List<DashboardDataResponse> getData() {
        return data;
    }

    public void setData(List<DashboardDataResponse> data) {
        this.data = data;
    }
}
