package com.samcrm.activity_manager;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.samcrm.Constants;
import com.samcrm.fragment.CartFragment;
import com.samcrm.fragment.CheckoutFragment;
import com.samcrm.fragment.OrderDetailsFragment;
import com.samcrm.fragment.OrderSuccessFragment;
import com.samcrm.fragment.OrdersFragment;
import com.samcrm.fragment.VendorItemListFragment;


public class FragmentFactory {
    public static BaseFragment getInstance(int id, Bundle bundle, Fragment lastLoadedFrag) {
        BaseFragment fragment = null;
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt("fragCode", id);
        switch (id) {
            case Constants.Fragment.VENDOR_ITEM_LIST:
                fragment = new VendorItemListFragment();
                break;
            case Constants.Fragment.CART:
                fragment = new CartFragment();
                break;
            case Constants.Fragment.CHECKOUT:
                fragment = new CheckoutFragment();
                break;
            case Constants.Fragment.ORDER_SUCCESS:
                fragment = new OrderSuccessFragment();
                break;
            case Constants.Fragment.Orders:
                fragment = new OrdersFragment();
                break;
            case Constants.Fragment.Orders_details:
                fragment = new OrderDetailsFragment();
                break;
            default:
                fragment = null;
                break;


        }

        if (bundle != null && fragment != null) {
            fragment.setFragCode(id);
            fragment.setArguments(bundle);
        }
        return fragment;
    }
}