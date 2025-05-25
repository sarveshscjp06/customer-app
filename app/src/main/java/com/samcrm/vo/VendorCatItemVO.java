package com.samcrm.vo;

import org.json.JSONObject;

import java.io.Serializable;

public class VendorCatItemVO implements Serializable {
    private int sequence;
    private String productDescription;
    private String offerPrice;
    private String productName;
    private int productCategoryId;
    private String productId;
    private int quantity;


    public VendorCatItemVO(JSONObject jsonObject) {
        this.sequence = jsonObject.optInt("sequence");
        this.productDescription = jsonObject.optString("productDescription");
        this.offerPrice = jsonObject.optString("offerPrice");
        this.productName = jsonObject.optString("productName");
        this.productCategoryId = jsonObject.optInt("productCategoryId");
        this.productId = jsonObject.optString("productId");
        this.quantity= jsonObject.optInt("quantity");
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(int productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}