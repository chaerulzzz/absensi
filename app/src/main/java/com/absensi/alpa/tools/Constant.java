package com.absensi.alpa.tools;

import com.absensi.alpa.BuildConfig;

public class Constant {

    public interface URL {
        String BASE_URL = BuildConfig.URL_BASE;
        String LOGIN = BuildConfig.URL_LOGIN;
        String FORGET = BuildConfig.URL_FORGET;
        String ATTENDANCE = BuildConfig.URL_ATTENDANCE;
        String REQUEST = BuildConfig.URL_REQUEST;
        String APPROVAL = BuildConfig.URL_APPROVAL;
        String USER = BuildConfig.URL_USER;
        String DASHBOARD = BuildConfig.URL_DASHBOARD;
    }

    public enum CREDENTIALS {
        SESSION,
        PASSWORD,
        ABSENT_STRICT,
        ABSENT_IMAGE,
        LETTER_SICK,
        NOTES,
        PRESENCE,
        EMAIL,
        NAME,
        USERID
    }
}
