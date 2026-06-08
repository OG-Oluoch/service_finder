package com.whebtos.e_chiro.ui.clientservices;

public class ClientService {
    private String serviceName;
    private String serviceDescription;
    private  String serviceImage;

    public ClientService(String serviceName, String serviceDescription, String serviceImage) {

        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.serviceImage = serviceImage;

    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public String getServiceImage() {
        return serviceImage;
    }


}