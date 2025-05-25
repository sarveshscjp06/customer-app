package com.samcrm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.samcrm.R;
import com.samcrm.activity_manager.BaseFragment;
import com.samcrm.adpater.ProductItemPagerAdapter;
import com.samcrm.network.NetworkConstant;
import com.samcrm.network.NetworkRequester;
import com.samcrm.utility.Utility;
import com.samcrm.vo.VendorCategoryVO;
import com.samcrm.vo.VendorVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class VendorItemListFragment extends BaseFragment {
    private Context context;
    private ProductItemPagerAdapter mDemoCollectionPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private VendorVO vendorVO;

    public VendorItemListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vendor_item, container, false);
        bindView(rootView);
        return rootView;
    }

    private void bindView(View rootView) {
        viewPager = rootView.findViewById(R.id.pager);
        tabLayout = rootView.findViewById(R.id.tabLayout);

        if (getArguments() != null) {
            vendorVO = (VendorVO) getArguments().getSerializable("vendorVO");
            getVendorCategoryList();
        }


    }

    private void setupViewPager(ArrayList<VendorCategoryVO> vendorVOList, Bundle arguments) {
        mDemoCollectionPagerAdapter = new ProductItemPagerAdapter(getChildFragmentManager(), vendorVOList,arguments);
        viewPager.setAdapter(mDemoCollectionPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void getVendorCategoryList() {

        JSONObject js = new JSONObject();
        try {
            js.put("vendorId", vendorVO.getVendorGroupId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject reqParams = Utility.getRequestParam(context, null);
        Utility.addParamJson(reqParams, js);
        new NetworkRequester.RequestBuilder(context)
                .setRequestType(NetworkConstant.ReqType.POST)
                .setUrl(NetworkConstant.samcrmProductCategorysOnly)
                .setListener(this)
                .setReqParams(reqParams)
                .setRequestTag(NetworkConstant.RequestTagType.PRODUCT_ONLY)
                .build()
                .sendRequest();


    }

    @Override
    public void onResponse(Object data, int requestTag) {
        Log.e("response", data.toString());
        switch (requestTag) {
            case NetworkConstant.RequestTagType.PRODUCT_ONLY:
                try {
                    ArrayList<VendorCategoryVO> vendorVOList = new ArrayList<>();
                    vendorVOList.clear();
                    JSONObject jsonObj = new JSONObject(data.toString());
                    JSONArray array = jsonObj.getJSONArray("tag");
                    for (int i = 0; i < array.length(); i++) {
                        vendorVOList.add(new VendorCategoryVO((JSONObject) array.get(i)));

                    }
                    setupViewPager(vendorVOList,getArguments());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    @Override
    public void onError(VolleyError error, int requestTag) {
        Log.e("response", error.toString());
    }

    @Override
    public String getToolbarTitle() {
        return vendorVO.getVendorTitle() != null ? vendorVO.getVendorTitle() : getString(R.string.app_name);
    }
}


