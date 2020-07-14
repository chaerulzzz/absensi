package com.absensi.alpa.api.endpoint.request;

import android.app.Activity;

import com.absensi.alpa.api.Api;
import com.absensi.alpa.api.Parser;
import com.absensi.alpa.api.endpoint.request.detail.RequestDetailResponse;
import com.absensi.alpa.api.endpoint.request.insert.RequestInsertRequest;
import com.absensi.alpa.api.endpoint.request.insert.RequestInsertResponse;
import com.absensi.alpa.api.endpoint.request.list.RequestListRequest;
import com.absensi.alpa.api.endpoint.request.list.RequestListResponse;
import com.absensi.alpa.tools.Constant;

import retrofit2.Call;

public class RequestService {

    public static Call<RequestListResponse> getListRequest(
            Activity activity,
            String url,
            String dateStart,
            String dateEnd
    ) {
        RequestListRequest request = new RequestListRequest();
        request.setDateStart(dateStart);
        request.setDateEnd(dateEnd);

        return Api.getRetrofit(activity)
                .create(Parser.class)
                .getRequestList(url, request);
    }

    public static Call<RequestInsertResponse> insertRequest(
            Activity activity,
            String url,
            String reason,
            String letter,
            String letterDate,
            String startDate,
            String endDate,
            String catId
    ) {
        RequestInsertRequest request = new RequestInsertRequest();

        if (url.equalsIgnoreCase(Constant.URL.SICK)) {
            request.setHospitalization("Y");
        }

        request.setCategoryId(catId);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setLetterDate(letterDate);
        request.setLetter(letter);
        request.setReason(reason);

        return Api.getRetrofit(activity)
                .create(Parser.class)
                .sendRequest(url, request);
    }

    public static Call<RequestDetailResponse> getDetailRequest(
            Activity activity,
            String url
    ){
        return Api.getRetrofit(activity)
                .create(Parser.class)
                .getDetail(url);
    }
}
