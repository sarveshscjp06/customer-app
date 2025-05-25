package com.samcrm;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Constants {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FragTransaction.ADD,
            FragTransaction.REPLACE,
            FragTransaction.NONE})
    public @interface FragTransaction {
        int ADD = 0;
        int REPLACE = 1;
        int NONE = -1;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Fragment.ITEM_LIST, Fragment.ORDER_SUCCESS, Fragment.Orders_details,
            Fragment.VENDOR_ITEM_LIST, Fragment.ADD_ITEM, Fragment.CART, Fragment.CHECKOUT, Fragment.Orders
    })
    public @interface Fragment {
        int ADD_ITEM = 2;
        int ITEM_LIST = 3;
        int VENDOR_ITEM_LIST = 4;
        int CART = 5;
        int CHECKOUT = 6;
        int ORDER_SUCCESS = 7;
        int Orders = 8;
        int Orders_details = 9;
    }

    //RESPONSE STATUS
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ResponseStatus.SUCCESS,
            ResponseStatus.ERROR,
            ResponseStatus.WARN})
    public @interface ResponseStatus {
        int ERROR = 0;
        int SUCCESS = 1;
        int WARN = 2;
    }


    @Retention(RetentionPolicy.SOURCE)
    @StringDef({Tag.DASHBOARD,
            Tag.Login,
            Tag.SubCategory,
            Tag.Checkout,
            Tag.PROFILE_DASHBOARD,
            Tag.PlaceOrderDetails})
    public @interface Tag {
        String DASHBOARD = "dashboard";
        String Login = "login";
        String SubCategory = "sub_category";
        String Checkout = "checkout";
        String SHIPPING_ADDRESS = "shipping_address";
        String PROFILE_DASHBOARD = "profile_dashboard";
        String PlaceOrderDetails = "PlaceOrderDetails";


    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({AddressType.NEW,
            AddressType.EDIT})
    public @interface AddressType {
        int EDIT = 0;
        int NEW = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({SharePrefName.USER_DETAILS, SharePrefName.CART_COUNT,})
    public @interface SharePrefName {
        String USER_DETAILS = "user_details";
        String CART_COUNT = "cart";
    }
}
