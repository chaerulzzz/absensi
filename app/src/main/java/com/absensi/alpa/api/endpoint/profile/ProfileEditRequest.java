package com.absensi.alpa.api.endpoint.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileEditRequest {

    @SerializedName("birth_date")
    @Expose
    private String birthDate;

    @SerializedName("birth_place")
    @Expose
    private String birthPlace;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("password_new")
    @Expose
    private String passwordNew;

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordNew() {
        return passwordNew;
    }

    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }
}
