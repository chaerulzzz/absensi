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

    @SerializedName("total_leave")
    @Expose
    private String totalLeave;

    @SerializedName("pending")
    @Expose
    private String pending;

    @SerializedName("late_in")
    @Expose
    private String lateIn;

    @SerializedName("user_name")
    @Expose
    private String userName;

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

    public String getTotalLeave() {
        return totalLeave;
    }

    public void setTotalLeave(String totalLeave) {
        this.totalLeave = totalLeave;
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public String getLateIn() {
        return lateIn;
    }

    public void setLateIn(String lateIn) {
        this.lateIn = lateIn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}