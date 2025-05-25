package com.samcrm.adpater;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.samcrm.fragment.VendorProductFragment;
import com.samcrm.vo.VendorCategoryVO;

import java.util.ArrayList;

public class ProductItemPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<VendorCategoryVO> vendorVOList;
    Bundle arguments;

    public ProductItemPagerAdapter(FragmentManager fm, ArrayList<VendorCategoryVO> vendorVOList, Bundle arguments) {
        super(fm);
        this.vendorVOList = vendorVOList;
        this.arguments = arguments;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new VendorProductFragment();
        arguments.putSerializable("vendorCategoryVO", vendorVOList.get(i));
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public int getCount() {
        return vendorVOList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return vendorVOList.get(position).getProductCategoryName();
    }
}