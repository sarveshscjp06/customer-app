package com.samcrm.vo;

import org.json.JSONObject;

import java.io.Serializable;


public class ShippingAddressVO implements Serializable {
    private String address;
    private String country_code;
    private String zipcode;
    private String name;
    private String individual_id;
    private String email;
    private String creation_date;
    private String mobile;
    private String dob;
    private String Comment;

    public ShippingAddressVO(JSONObject jsonObject) {
        this.address = jsonObject.optString("address");
        this.dob = jsonObject.optString("dob");
        this.country_code = jsonObject.optString("country_code");
        this.zipcode = jsonObject.optString("zipcode");
        this.name = jsonObject.optString("name");
        this.mobile = jsonObject.optString("mobile");
        this.creation_date = jsonObject.optString("creation_date");
        this.email = jsonObject.optString("email");
        this.individual_id = jsonObject.optString("individual_id");
        this.mobile = jsonObject.optString("phoneNumber");
        this.name = jsonObject.optString("name");
    }

    public ShippingAddressVO() {

    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndividual_id() {
        return individual_id;
    }

    public void setIndividual_id(String individual_id) {
        this.individual_id = individual_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}