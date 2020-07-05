package com.absensi.alpa.api.endpoint.request.insert;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestInsertDataResponse {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("number")
    @Expose
    private String number;

    @SerializedName("date_start")
    @Expose
    private String dateStart;

    @SerializedName("date_end")
    @Expose
    private String dateEnd;

    @SerializedName("reason")
    @Expose
    private String reason;

    @SerializedName("letter_status")
    @Expose
    private String letterStatus;

    @SerializedName("letter_date")
    @Expose
    private String letterDate;

    @SerializedName("letter")
    @Expose
    private String letter;

    @SerializedName("approval_date")
    @Expose
    private String approvalDate;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("requester")
    @Expose
    private String requester;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLetterStatus() {
        return letterStatus;
    }

    public void setLetterStatus(String letterStatus) {
        this.letterStatus = letterStatus;
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

    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }
}
