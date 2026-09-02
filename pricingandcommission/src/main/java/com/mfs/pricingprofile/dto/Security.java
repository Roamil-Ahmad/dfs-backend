/*
Author Name: romail.ahmed

Project Name: integration

Package Name:org.mfs.integration.dto

Class Name: Security

Date and Time:2/6/2023 4:28 PM

Version:1.0
*/
package com.mfs.pricingprofile.dto;

public class Security {
    private String userName;
    private String password;
    private String securityToken;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }
}
