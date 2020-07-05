package com.absensi.alpa.api.endpoint.request.insert;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestInsertRequest {

    @SerializedName("category")
    @Expose
    private String categoryId;

    @SerializedName("start_date")
    @Expose
    private String startDate;

    @SerializedName("end_date")
    @Expose
    private String endDate;

    @SerializedName("reason")
    @Expose
    private String reason;

    @SerializedName("hospitalization")
    @Expose
    private String hospitalization;

    @SerializedName("letter_date")
    @Expose
    private String letterDate;

    @SerializedName("letter")
    @Expose
    private String letter;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getHospitalization() {
        return hospitalization;
    }

    public void setHospitalization(String hospitalization) {
        this.hospitalization = hospitalization;
    }

    public String getLetterDate() {
        return letterDate;
    }

    public void setLetterDate(String letterDate) {
        this.letterDate = letterDate;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
