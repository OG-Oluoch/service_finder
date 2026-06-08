package com.whebtos.e_chiro.models;

import java.time.LocalDateTime;
import java.util.Date;

public class User {

    private String userName;

    private String phoneNumber;

    private String emailAddress;

    private String password;

    private String confirmPassword;

    private String clientType;

    private String country;

    private String county;

    private String subCounty;

    private String createdByUserName;

    private String dateOfBirth;

    private String backIdImage;

    private String frontIdImage;

    private String id;

    private String idNumber;

    private String image;

    private String name;

    private String accessToken;

    private Date accessTokenExpiryDate;

    public Date getAccessTokenExpiryDate() {
        return accessTokenExpiryDate;
    }

    public void setAccessTokenExpiryDate(Date accessTokenExpiryDate) {
        this.accessTokenExpiryDate = accessTokenExpiryDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getSubCounty() {
        return subCounty;
    }

    public void setSubCounty(String subCounty) {
        this.subCounty = subCounty;
    }

    public String getCreatedByUserName() {
        return createdByUserName;
    }

    public void setCreatedByUserName(String createdByUserName) {
        this.createdByUserName = createdByUserName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBackIdImage() {
        return backIdImage;
    }

    public void setBackIdImage(String backIdImage) {
        this.backIdImage = backIdImage;
    }

    public String getFrontIdImage() {
        return frontIdImage;
    }

    public void setFrontIdImage(String frontIdImage) {
        this.frontIdImage = frontIdImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
