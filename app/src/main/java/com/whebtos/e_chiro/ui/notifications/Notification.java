package com.whebtos.e_chiro.ui.notifications;

public class Notification {
    private String title;
    private String content;
    private String id;
    private String dateCreated;
    private String serviceProviderID;
    private String serviceProviderEmail;

    public Notification(String id, String title, String content, String dateCreated, String serviceProviderID, String serviceProviderEmail) {

        this.id = id;
        this.title = title;
        this.content = content;
        this.dateCreated = dateCreated;
        this.serviceProviderID = serviceProviderID;
        this.serviceProviderEmail = serviceProviderEmail;


    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setServiceProviderID(String serviceProviderID) {
        this.serviceProviderID = serviceProviderID;
    }

    public void setServiceProviderEmail(String serviceProviderEmail) {
        this.serviceProviderEmail = serviceProviderEmail;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
    public String getId()
    {
        return id;
    }

    public String getDateCreated()
    {
        return dateCreated;
    }

    public String getServiceProviderID() {
        return serviceProviderID;
    }

    public String getServiceProviderEmail() {
        return serviceProviderEmail;
    }
}