package com.samcrm.vo;

import org.json.JSONObject;

import java.io.Serializable;

public class VendorVO implements Serializable {
    private String vendorCategoryDescription;
    private String vendorRegistrationNo;
    private String vendorLogoName;
    private String vendorCategoryName;
    private String mobile;
    private String vendorGroupId;
    private String individual_id;
    private String zipcode;
    private String country_code;
    private String vendorSubtypeDescription;
    private String name;
    private String vendorLogoUrl;
    private String email;
    private String vendorTitle;

    public VendorVO(JSONObject jsonObject) {
        this.vendorCategoryDescription = jsonObject.optString("vendorCategoryDescription");
        this.vendorRegistrationNo = jsonObject.optString("vendorRegistrationNo");
        this.vendorLogoName = jsonObject.optString("vendorLogoName");
        this.vendorCategoryName = jsonObject.optString("vendorCategoryName");
        this.mobile = jsonObject.optString("mobile");
        this.vendorGroupId = jsonObject.optString("vendorGroupId");
        this.individual_id = jsonObject.optString("individual_id");
        this.zipcode = jsonObject.optString("zipcode");
        this.country_code = jsonObject.optString("country_code");
        this.vendorSubtypeDescription = jsonObject.optString("vendorSubtypeDescription");
        this.name = jsonObject.optString("name");
        this.vendorLogoUrl = jsonObject.optString("vendorLogoUrl");
        this.vendorTitle = jsonObject.optString("vendorTitle");
        this.email = jsonObject.optString("email");
    }

    public String getVendorCategoryDescription() {
        return vendorCategoryDescription;
    }

    public void setVendorCategoryDescription(String vendorCategoryDescription) {
        this.vendorCategoryDescription = vendorCategoryDescription;
    }

    public String getVendorRegistrationNo() {
        return vendorRegistrationNo;
    }

    public void setVendorRegistrationNo(String vendorRegistrationNo) {
        this.vendorRegistrationNo = vendorRegistrationNo;
    }

    public String getVendorLogoName() {
        return vendorLogoName;
    }

    public void setVendorLogoName(String vendorLogoName) {
        this.vendorLogoName = vendorLogoName;
    }

    public String getVendorCategoryName() {
        return vendorCategoryName;
    }

    public void setVendorCategoryName(String vendorCategoryName) {
        this.vendorCategoryName = vendorCategoryName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVendorGroupId() {
        return vendorGroupId;
    }

    public void setVendorGroupId(String vendorGroupId) {
        this.vendorGroupId = vendorGroupId;
    }

    public String getIndividual_id() {
        return individual_id;
    }

    public void setIndividual_id(String individual_id) {
        this.individual_id = individual_id;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getVendorSubtypeDescription() {
        return vendorSubtypeDescription;
    }

    public void setVendorSubtypeDescription(String vendorSubtypeDescription) {
        this.vendorSubtypeDescription = vendorSubtypeDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendorLogoUrl() {
        return vendorLogoUrl;
    }

    public void setVendorLogoUrl(String vendorLogoUrl) {
        this.vendorLogoUrl = vendorLogoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVendorTitle() {
        return vendorTitle;
    }

    public void setVendorTitle(String vendorTitle) {
        this.vendorTitle = vendorTitle;
    }
}