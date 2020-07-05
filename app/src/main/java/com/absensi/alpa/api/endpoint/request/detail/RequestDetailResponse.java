package com.absensi.alpa.api.endpoint.request.detail;

import com.absensi.alpa.api.endpoint.GeneralResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RequestDetailResponse extends GeneralResponse implements Serializable {

    @SerializedName("data")
    @Expose
    private List<RequestDetailDataResponse> data;

    public List<RequestDetailDataResponse> getData() {
        return data;
    }

    public void setData(List<RequestDetailDataResponse> data) {
        this.data = data;
    }
}
