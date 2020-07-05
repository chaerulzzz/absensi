package com.absensi.alpa.api.endpoint.request.insert;

import com.absensi.alpa.api.endpoint.GeneralResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestInsertResponse extends GeneralResponse implements Serializable {

    @SerializedName("data")
    @Expose
    private RequestInsertDataResponse data;

    public RequestInsertDataResponse getData() {
        return data;
    }

    public void setData(RequestInsertDataResponse data) {
        this.data = data;
    }
}
