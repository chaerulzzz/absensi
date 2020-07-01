package com.absensi.alpa.api.endpoint.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardDataResponse {

    @SerializedName("time_in")
    @Expose
    private String timeIn;

    @SerializedName("time_out")
    @Expose
    private String timeOut;

    @SerializedName("alpha")
    @Expose
    private String alpha;

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getAlpha() {
        return alpha;
    }

    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }
}
