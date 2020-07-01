package com.absensi.alpa.api.endpoint.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestListDataResponse {

    @SerializedName("id")
    @Expose
    private String requestId;

    @SerializedName("type")
    @Expose
    private String requestType;

    @SerializedName("category")
    @Expose
    private String requestCategory;

    @SerializedName("date_start")
    @Expose
    private String requestDateStart;

    @SerializedName("date_end")
    @Expose
    private String requestDateEnd;

    @SerializedName("status")
    @Expose
    private String requestStatus;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestCategory() {
        return requestCategory;
    }

    public void setRequestCategory(String requestCategory) {
        this.requestCategory = requestCategory;
    }

    public String getRequestDateStart() {
        return requestDateStart;
    }

    public void setRequestDateStart(String requestDateStart) {
        this.requestDateStart = requestDateStart;
    }

    public String getRequestDateEnd() {
        return requestDateEnd;
    }

    public void setRequestDateEnd(String requestDateEnd) {
        this.requestDateEnd = requestDateEnd;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
}
