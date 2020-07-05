package com.absensi.alpa.api.endpoint.approval.list;

import com.absensi.alpa.api.endpoint.GeneralResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ApprovalListResponse extends GeneralResponse implements Serializable {

    @SerializedName("data")
    @Expose
    private List<ApprovalListDataResponse> data;

    public List<ApprovalListDataResponse> getData() {
        return data;
    }

    public void setData(List<ApprovalListDataResponse> data) {
        this.data = data;
    }
}
