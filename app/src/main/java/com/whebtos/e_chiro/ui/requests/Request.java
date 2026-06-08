package com.whebtos.e_chiro.ui.requests;

public class Request {
    private String serviceDescription;
    private String serviceLatitude;
    private  String serviceLongitude;

    public Request(String serviceDescription, String serviceLatitude, String serviceLongitude) {

        this.serviceDescription = serviceDescription;
        this.serviceLatitude = serviceLatitude;
        this.serviceLongitude = serviceLongitude;

    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public String getServiceLatitude(){
        return serviceLatitude;
    }

    public String getServiceLongitude(){
        return serviceLongitude;
    }


}