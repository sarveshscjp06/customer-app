package com.samcrm.utility;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.samcrm.Constants;
import com.samcrm.R;
import com.samcrm.activity.CartActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Utility {
    public Map<Integer, String> convertToMap(JSONObject object) throws JSONException {
        Map<Integer, String> map = new HashMap<>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);
            map.put(Integer.valueOf(key), String.valueOf(value));
        }
        return map;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    public static String getDeviceImei(Context ctx) {
        Log.e("android_ID",Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID));
        return Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String fetchValueFromReqParam(String key, JSONObject reqParam) {
        JSONArray paramArr = reqParam.optJSONArray("params");
        if (paramArr != null) {
            for (int i = 0; i < paramArr.length(); i++) {
                try {
                    JSONObject paramJson = paramArr.getJSONObject(i);
                    if (paramJson.has(key)) {
                        return paramJson.optString(key);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static JSONObject addParamJson(JSONObject reqParam, JSONObject jsonObject) {
        JSONArray paramArr = reqParam.optJSONArray("params");
        if (jsonObject != null) {
            paramArr.put(jsonObject);
        }
        return reqParam;
    }

    public static JSONObject getRequestParam(Context ctx, String basic) {
        JSONObject reqParam = new JSONObject();
        try {
            JSONObject authJson = new JSONObject();
            authJson.put("Authorization", "Bearer " + basic);
            reqParam.put("auth", authJson);
            reqParam.put("params", new JSONArray());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reqParam;
    }

    public static String getVendorId(Context context) {
        String vendorId =null;
        try {
            JSONObject individualDataMap = new JSONObject(SharedPref.getInstance(context).getString(Constants.SharePrefName.USER_DETAILS));
            vendorId = individualDataMap.getJSONObject("status").optString("vendor_group_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return vendorId;
    }
    public static String getOrderId(Context context) {
        String orderId =null;
        try {
            JSONObject individualDataMap = new JSONObject(SharedPref.getInstance(context).getString(Constants.SharePrefName.USER_DETAILS));
            orderId = individualDataMap.getJSONObject("status").optString("order_id "); //Todo change change Json tag by S
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return orderId;
    }
    public static String getCustomerID(Context context) {
        String custId =null;
        try {
            JSONObject individualDataMap = new JSONObject(SharedPref.getInstance(context).getString(Constants.SharePrefName.USER_DETAILS));
            custId = individualDataMap.getJSONObject("status").optString("individual_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return custId;
    }
    public static String getZipCode(Context context) {
        String zipCode =null;
        try {
            JSONObject individualDataMap = new JSONObject(SharedPref.getInstance(context).getString(Constants.SharePrefName.USER_DETAILS));
            zipCode = individualDataMap.getJSONObject("status").optString("zipCode");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return zipCode;
    }
    public static void onCartMenuItemClicked(Context context) {
        int productCount = Utility.getCartCount(context);
        if (productCount <= 0) {
            Utility.t(context, "Your Shopping Cart is empty");
        } else {
            Intent intent = new Intent(context, CartActivity.class);
            context.startActivity(intent);
        }


    }
    public static void clearCartCount(Context context) {
        SharedPref sharedPref = new SharedPref(context);
        sharedPref.clear(Constants.SharePrefName.CART_COUNT);
    }
    public static int getCartCount(Context context) {
        SharedPref sharedPref = new SharedPref(context);
        return sharedPref.getInt(Constants.SharePrefName.CART_COUNT);
    }
    public static void setCartCount(Context context,int count) {
        SharedPref sharedPref = new SharedPref(context);
        sharedPref.saveInt(Constants.SharePrefName.CART_COUNT,count);


    }
    public static void updateCartCounterUI(Toolbar toolbar, Context context) {
        MenuItem cartMenuItem = toolbar.getMenu().findItem(R.id.cart);
        if (cartMenuItem != null) {
            final FrameLayout cartActionView = (FrameLayout) MenuItemCompat.getActionView(cartMenuItem);
            TextView cartCount = cartActionView.findViewById(R.id.view_alert_count_textview);
            int productCount = Utility.getCartCount(context);
            if (productCount <= 0) {
                cartCount.setVisibility(View.INVISIBLE);
            } else {
                cartCount.setVisibility(View.VISIBLE);
                cartCount.setText(String.valueOf(productCount));
            }
        }
    }
    public static void t(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
}
