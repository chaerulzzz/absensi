package com.absensi.alpa.api.endpoint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GeneralRequest implements Serializable {

    @SerializedName("ts")
    @Expose
    private Long timestamp;

    @SerializedName("sc")
    @Expose
    private String securityCode;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
}

