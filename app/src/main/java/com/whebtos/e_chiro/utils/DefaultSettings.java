package com.whebtos.e_chiro.utils;

public interface DefaultSettings {

    public static final String URL ="https://backend.wencetechnologies.com/whebtos/api " ;//"https://development.esriea.com/whebtos/api";

    public static final String URL_REGISTER_USER = "/clients/create";

    public static final String URL_USER_BY_USERNAME =  "/clients/getbyusername"; //"Whebtouser/getbyusername";

    public static final String URL_USER_LOGIN = "/authenticate/";

    public static final String URL_CREATE_CLIENT_SERVICE = "/clientservices/create";

    public static final String URL_LIST_CLIENT_SERVICE_BY_USERNAME = "/clientservices/list/";

    public static final String URL_SERVICE_CATEGORIES = "/servicecategories/listwithnoparent";

    public static final String URL_SERVICES_BY_CATEGORY_ID = "/services/listbyservicecategoryid/";

    public static final String URL_ALL_SERVICES = "/service";

    public static final String URL_SERVICE_SUB_CATEGORIES = "/servicecategories/listbyparentid/";

    public static final String URL_SERVICE_CATEGORY_IMAGE = "/files/servicecategoryimage/";

    public static final String URL_SERVICE_IMAGE = "/files/serviceimage/";

    String URL_GET_GALLERY = "/";

    String DATABASE_NAME = "EchiroDB";

    String ALERTS_TABLE = "alerts";

    String LOGINS_TABLE = "logins";

    //String USER_ID_TABLE = "userToken";

    String URL_CONTACT_US = "/api/v1/customer/contact";

    String URL_FEEDBACK = "/api/v1/customer/feedback";

    String URL_SURVEY_BY_ID = "/api/v1/surveys/";

    String URL_SURVEY_RESPONSE = "/api/v1/surveys/response/post";

    String SURVEY_ID = "/fetch";
}
