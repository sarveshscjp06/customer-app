package com.samcrm.vo;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemCartVO implements Serializable {
    private String product_name;
    private int quantity;
    private int item_id;
    private int price;
    private int product_id;
    private int order_id;
    private int discount;
    private String description;

    public ItemCartVO() {
    }

    public ItemCartVO(JSONObject jsonObject) {
        this.quantity = jsonObject.optInt("quantity");
        this.item_id = jsonObject.optInt("item_id");
        this.price = jsonObject.optInt("price");
        this.product_id = jsonObject.optInt("product_id");
        this.order_id = jsonObject.optInt("order_id");
        this.product_name = jsonObject.optString("product_name");
        this.discount = jsonObject.optInt("discount");
        this.description = jsonObject.optString("description");
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }


    public  int totalDis(ArrayList<ItemCartVO> cartList) {
        int sum = 0;

        for (ItemCartVO ItemCartVO : cartList) {
            sum += ItemCartVO.getDiscount();
        }
        return sum;
    }

    public  int totalPrice(ArrayList<ItemCartVO> cartList) {
        int sum = 0;

        for (ItemCartVO itemCartVO : cartList) {
            sum += itemCartVO.getQuantity()*itemCartVO.getPrice();
        }
        return sum;
    }
}
