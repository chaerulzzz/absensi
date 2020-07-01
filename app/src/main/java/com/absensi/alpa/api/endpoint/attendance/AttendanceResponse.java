package com.absensi.alpa.api.endpoint.attendance;

import com.absensi.alpa.api.endpoint.GeneralResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AttendanceResponse extends GeneralResponse implements Serializable {

    @SerializedName("data")
    @Expose
    private List<AttendanceDataResponse> data;

    public List<AttendanceDataResponse> getData() {
        return data;
    }

    public void setData(List<AttendanceDataResponse> data) {
        this.data = data;
    }
}
