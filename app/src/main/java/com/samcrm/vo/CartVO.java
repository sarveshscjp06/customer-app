package com.samcrm.vo;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class CartVO implements Serializable {
    private String zipcode;
    private String country;
    private String recepient;
    private String mobile;
    private int address_id;
    private String addresstype;
private  String street;
    private String delivery_address;
    private String comments;
    private String delivery_time;
    private String order_date;
    private String delivery_date;
    private int price;
    private int vendor_id;
    private String delivery_person;
    private int customer_id;
    private int order_id;
    private ArrayList<ItemCartVO> itemCartVOS = new ArrayList<>();

    public CartVO(JSONObject jsonObject) {
        try {
            JSONObject customerAddress = jsonObject.getJSONObject("customerAddress");
            this.zipcode = customerAddress.optString("zipcode");
            this.street = customerAddress.optString("street");
            this.country = customerAddress.optString("country");
            this.recepient = customerAddress.optString("recepient");
            this.mobile = customerAddress.optString("mobile");
            this.address_id = customerAddress.optInt("address_id");
            this.addresstype = customerAddress.optString("addresstype");

            this.delivery_address = jsonObject.optString("delivery_address");
            this.comments = jsonObject.optString("comments");
            this.delivery_time = jsonObject.optString("delivery_time");
            this.order_date = jsonObject.optString("order_date");
            this.delivery_date = jsonObject.optString("delivery_date");
            this.price = jsonObject.optInt("price");
            this.order_id = jsonObject.optInt("order_id");
            this.vendor_id = jsonObject.optInt("vendor_id");
            this.delivery_person = jsonObject.optString("delivery_person");
            this.customer_id = jsonObject.optInt("customer_id");


            JSONArray array = jsonObject.getJSONArray("items");
            for (int i = 0; i < array.length(); i++) {
                itemCartVOS.add(new ItemCartVO((JSONObject) array.get(i)));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRecepient() {
        return recepient;
    }

    public void setRecepient(String recepient) {
        this.recepient = recepient;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public String getAddresstype() {
        return addresstype;
    }

    public void setAddresstype(String addresstype) {
        this.addresstype = addresstype;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(int vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getDelivery_person() {
        return delivery_person;
    }

    public void setDelivery_person(String delivery_person) {
        this.delivery_person = delivery_person;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public ArrayList<ItemCartVO> getItemCartVOS() {
        return itemCartVOS;
    }

    public void setItemCartVOS(ArrayList<ItemCartVO> itemCartVOS) {
        this.itemCartVOS = itemCartVOS;
    }
}
