package com.whebtos.e_chiro.ui.clienthome;

import java.net.ContentHandler;

public class ListItem {

    private String serviceName;
    private String serviceDescription;
    private  String serviceImage;
    private String serviceID;

    public ListItem(String serviceName, String serviceDescription, String serviceImage, String serviceID) {

        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.serviceImage = serviceImage;
        this.serviceID = serviceID;


    }
//
//    public ListItem(String createdById) {
//        this.UserID = createdById;
//    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public String getServiceImage() {
        return serviceImage;
    }

    public String getServiceID()
    {
        return serviceID;
    }


}