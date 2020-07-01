package com.absensi.alpa.api.endpoint.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardRequest {

    @SerializedName("date")
    @Expose
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
