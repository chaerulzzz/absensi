package com.absensi.alpa.api.endpoint.approval;

import android.app.Activity;

import com.absensi.alpa.api.Api;
import com.absensi.alpa.api.Parser;
import com.absensi.alpa.api.endpoint.approval.list.ApprovalListResponse;
import com.absensi.alpa.api.endpoint.approval.process.ApprovalProcessRequest;
import com.absensi.alpa.api.endpoint.approval.process.ApprovalProcessResponse;

import retrofit2.Call;

public class ApprovalService {
    public static Call<ApprovalListResponse> getApprovalList(
            Activity activity,
            String url
    ){
        return Api.getRetrofit(activity)
                .create(Parser.class)
                .getList(url);
    }

    public static Call<ApprovalProcessResponse> sendProcess(
            Activity activity,
            String url,
            String id,
            String status,
            String notes
    ) {
        ApprovalProcessRequest request = new ApprovalProcessRequest();
        request.setId(id);
        request.setStatus(status);
        request.setNotes(notes);
        request.setMethod("PUT");

        return Api.getRetrofit(activity)
                .create(Parser.class)
                .sendApproval(url + "/" + id, request);
    }
}

