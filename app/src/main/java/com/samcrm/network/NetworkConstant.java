package com.samcrm.network;


import android.support.annotation.IntDef;
import android.widget.RelativeLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class NetworkConstant {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ReqType.GET, ReqType.POST, ReqType.PUT, ReqType.DELETE})
    public @interface ReqType {
        int GET = 0;
        int POST = 1;
        int PUT = 2;
        int DELETE = 3;
    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Timeout.REQUEST_TIME})
    public @interface Timeout {
        int REQUEST_TIME = 50000;
    }

//    public static final String BASE_URL = "http://192.168.1.6:8080/resources/";
    public static final String BASE_URL = "http://140.238.250.40:8080/resources/";

    public static final String userDataURL = NetworkConstant.BASE_URL + "login/samcrmUserData";
    public static final String loginURL = NetworkConstant.BASE_URL + "login/samcrmAuthentication";

    public static final String samcrmUpdateProductCategory = BASE_URL + "vendor/samcrmUpdateProductCategory";

    public static final String samcrmUpdateProductAndService = BASE_URL + "vendor/samcrmUpdateProductAndService";

    public static final String samcrmProductItems = BASE_URL + "vendor/samcrmProductItems";
    public static final String samcrmVendorsList = BASE_URL + "login/samcrmVendorsList";
    public static final String samcrmProductCategorysOnly = BASE_URL + "vendor/samcrmProductCategorysOnly";

    public static final String samcrmCheckInventory = BASE_URL + "vendor/samcrmCheckInventory";
    public static final String samcrmBookOrder = BASE_URL + "order/samcrmBookOrder";
    public static final String samcrmOrdersList = BASE_URL + "order/samcrmOrdersList";
    public static final String samcrmListContacts = BASE_URL + "vendor/samcrmListContacts";
    public static final String samcrmAddContact = BASE_URL + "vendor/samcrmAddContact";

    public static final String samcrmCheckInventoryAndUpdateQuantity = BASE_URL + "order/samcrmCheckInventoryAndUpdateQuantity";

    public static final String samcrmDiscountCoupon = BASE_URL + "order/samcrmDiscountCoupon";

    public static final String samcrmConfirmOrder = BASE_URL + "order/samcrmConfirmOrder";


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({RequestTagType.PRODUCT_LIST,
            RequestTagType.PRODUCT_ONLY,
            RequestTagType.VENDOR_LIST,
            RequestTagType.samcrmCheckInventory,
            RequestTagType.userDataURL,
            RequestTagType.samcrmBookOrder,
            RequestTagType.samcrmOrdersList,
            RequestTagType.samcrmAddContact,
            RequestTagType.samcrmListContacts,
            RequestTagType.login,
            RequestTagType.samcrmCheckInventoryAndUpdateQuantity,
            RequestTagType.samcrmDiscountCoupon,
            RequestTagType.samcrmConfirmOrder
    })
    public @interface RequestTagType {
        int PRODUCT_LIST = 0;
        int userDataURL = 1;
        int login = 2;
        int VENDOR_LIST = 3;
        int PRODUCT_ONLY = 4;
        int samcrmCheckInventory = 5;
        int samcrmBookOrder = 6;
        int samcrmOrdersList = 7;
        int samcrmListContacts = 8;
        int samcrmAddContact = 9;
        int samcrmCheckInventoryAndUpdateQuantity = 10;
        int samcrmDiscountCoupon = 11;
        int samcrmConfirmOrder = 12;

    }
}
