package com.absensi.alpa.api.endpoint.request.list;

import com.absensi.alpa.api.endpoint.GeneralResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RequestListResponse extends GeneralResponse implements Serializable {

    @SerializedName("data")
    @Expose
    private List<RequestListDataResponse> data;

    public List<RequestListDataResponse> getData() {
        return data;
    }

    public void setData(List<RequestListDataResponse> data) {
        this.data = data;
    }
}
