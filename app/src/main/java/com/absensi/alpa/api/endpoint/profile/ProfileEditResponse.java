package com.absensi.alpa.api.endpoint.profile;

import com.absensi.alpa.api.endpoint.GeneralResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileEditResponse extends GeneralResponse {

    @SerializedName("data")
    @Expose
    private ProfileDataResponse data;

    public ProfileDataResponse getData() {
        return data;
    }

    public void setData(ProfileDataResponse data) {
        this.data = data;
    }
}
