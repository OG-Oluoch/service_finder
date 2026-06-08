package com.whebtos.e_chiro.utils;

public interface DefaultSettings {

    public static final String URL = "https://178.62.19.24:50082/api";

    public static final String URL_REGISTER_USER = "/clients/create";

    public static final String URL_USER_LOGIN = "/authenticate/";

    public static final String URL_SERVICE_CATEGORIES = "/servicecategories/listwithnoparent";

    public static final String URL_SERVICES = "/services/listbyservicecategoryid/";

    public static final String URL_SERVICE_SUB_CATEGORIES = "/servicecategories/listbyparentid/";

    public static final String URL_SERVICE_CATEGORY_IMAGE = "/files/servicecategoryimage/";

    public static final String URL_SERVICE_IMAGE = "/files/serviceimage/";

    String URL_GET_GALLERY = "/";

    String DATABASE_NAME = "KonzaDB";

    String ALERTS_TABLE = "alerts";

    String LOGINS_TABLE = "logins";

    String URL_CONTACT_US = "/api/v1/customer/contact";

    String URL_FEEDBACK = "/api/v1/customer/feedback";

    String URL_SURVEY_BY_ID = "/api/v1/surveys/";

    String URL_SURVEY_RESPONSE = "/api/v1/surveys/response/post";

    String SURVEY_ID = "/fetch";
}
