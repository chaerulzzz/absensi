package com.absensi.alpa.tools;

import com.absensi.alpa.BuildConfig;

public class Constant {

    public interface URL {
        String BASE_URL = BuildConfig.URL_BASE;
        String LOGIN = BuildConfig.URL_LOGIN;
        String ATTENDANCE = BuildConfig.URL_ATTENDANCE;
        String ATTENDANCE_LIST = BuildConfig.URL_ATTENDANCE_LIST;
        String REQUEST = BuildConfig.URL_REQUEST;
        String PERMIT = BuildConfig.URL_REQUEST_PERMIT;
        String LEAVE = BuildConfig.URL_REQUEST_LEAVE;
        String SICK = BuildConfig.URL_REQUEST_SICK;
        String OVERTIME = BuildConfig.URL_REQUEST_OVERTIME;
        String APPROVAL = BuildConfig.URL_APPROVAL;
        String USER = BuildConfig.URL_USER;
        String DASHBOARD = BuildConfig.URL_DASHBOARD;
    }

    public enum CREDENTIALS {
        SESSION,
        PASSWORD,
        EMAIL,
        NAME,
        USERID,
        BIRTH_DATE,
        BIRTH_PLACE
    }
}
