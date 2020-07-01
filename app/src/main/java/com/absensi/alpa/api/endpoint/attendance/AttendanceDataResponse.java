package com.absensi.alpa.api.endpoint.attendance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceDataResponse {

    @SerializedName("date_in")
    @Expose
    private String dateIn;

    @SerializedName("time_in")
    @Expose
    private String timeIn;

    @SerializedName("time_out")
    @Expose
    private String timeOut;

    @SerializedName("status")
    @Expose
    private String status;

    public String getDateIn() {
        return dateIn;
    }

    public void setDateIn(String dateIn) {
        this.dateIn = dateIn;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
