package com.samcrm.vo;

import org.json.JSONObject;

import java.io.Serializable;

public class VendorCategoryVO implements Serializable {
    private String sequence;
    private String productCategoryId;
    private String productCategoryName;
    private String productCategoryDescription;


    public VendorCategoryVO(JSONObject jsonObject) {
        this.sequence = jsonObject.optString("sequence");
        this.productCategoryId = jsonObject.optString("productCategoryId");
        this.productCategoryName = jsonObject.optString("productCategoryName");
        this.productCategoryDescription = jsonObject.optString("productCategoryDescription");
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(String productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public String getProductCategoryDescription() {
        return productCategoryDescription;
    }

    public void setProductCategoryDescription(String productCategoryDescription) {
        this.productCategoryDescription = productCategoryDescription;
    }
}