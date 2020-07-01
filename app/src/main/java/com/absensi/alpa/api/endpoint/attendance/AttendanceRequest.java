package com.absensi.alpa.api.endpoint.attendance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceRequest {

    @SerializedName("date_start")
    @Expose
    private String dateStart;

    @SerializedName("date_end")
    @Expose
    private String dateEnd;

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }
}
