package com.absensi.alpa.api.endpoint.forget;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgetRequest {

    @SerializedName("nip")
    @Expose
    private String nip;

    @SerializedName("email")
    @Expose
    private String email;

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
