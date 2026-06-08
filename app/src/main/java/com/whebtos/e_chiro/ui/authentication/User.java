package com.whebtos.e_chiro.ui.authentication;

import java.util.List;

public class User {

    public String name, phone, email, username;

    public User ()
    {

    }

    public User(String name,String phone, String email, String username)
    {
        this.name = name;

        this.phone = phone;

        this.email = email;

        this.username = username;
    }

}
//
//public class User {
//    private String id;
//    private String userName;
//    private String normalizedUserName;
//    private String email;
//    private String normalizedEmail;
//    private int emailConfirmed;
//    private String passwordHash;
//    private String securityStamp;
//    private String concurrencyStamp;
//    private String phoneNumber;
//    private int phoneNumberConfirmed;
//    private int twoFactorEnabled;
//    private String lockoutEnd;
//    private int lockoutEnabled;
//    private int accessFailedCount;
//    private List<Object> userclaims = null;
//    private List<Object> userlogins = null;
//    private List<Object> usertokens = null;
//
//    public String getId() {
//        return id;
//    }
//}

