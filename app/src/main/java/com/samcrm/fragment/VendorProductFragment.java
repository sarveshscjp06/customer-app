package com.samcrm.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.samcrm.R;
import com.samcrm.activity_manager.BaseFragment;
import com.samcrm.adpater.VendorCatItemListAdapter;
import com.samcrm.network.NetworkConstant;
import com.samcrm.network.NetworkRequester;
import com.samcrm.network.NetworkResponseListener;
import com.samcrm.utility.Utility;
import com.samcrm.vo.VendorCatItemVO;
import com.samcrm.vo.VendorCategoryVO;
import com.samcrm.vo.VendorVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VendorProductFragment extends BaseFragment implements NetworkResponseListener, VendorCatItemListAdapter.OnItemClickListener {
    private VendorCatItemListAdapter adapter;
    private List<VendorCatItemVO> itemVOS;
    private VendorCategoryVO vendorCategoryVO;
    private RecyclerView recyclerView;
    private VendorVO vendorVO;
    private TextView notFoundTv;
    private ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item, container, false);
        bindView(rootView);
        return rootView;
    }

    private void bindView(View rootView) {
        notFoundTv = rootView.findViewById(R.id.no_data);
        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));


        if (getArguments() != null) {
            vendorCategoryVO = (VendorCategoryVO) getArguments().getSerializable("vendorCategoryVO");
            vendorVO = (VendorVO) getArguments().getSerializable("vendorVO");
            if (vendorCategoryVO != null && vendorVO != null) {
                getCatItemList();
            }

        }
  }

    private void getCatItemList() {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject js = new JSONObject();
        try {
            js.put("vendorId", vendorVO.getVendorGroupId());
            js.put("productCategoryId", vendorCategoryVO.getProductCategoryId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject reqParams = Utility.getRequestParam(getContext(), null);
        Utility.addParamJson(reqParams, js);
        new NetworkRequester.RequestBuilder(getContext())
                .setRequestType(NetworkConstant.ReqType.POST)
                .setUrl(NetworkConstant.samcrmProductItems)
                .setListener(this)
                .setReqParams(reqParams)
                .setRequestTag(NetworkConstant.RequestTagType.PRODUCT_LIST)
                .build()
                .sendRequest();


    }

    @Override
    public void onResponse(Object data, int requestTag) {
        progressBar.setVisibility(View.GONE);
        Log.e("response", data.toString());
        try {
            itemVOS = new ArrayList<>();
            itemVOS.clear();
            JSONObject jsonObj = new JSONObject(data.toString());
            JSONArray array = jsonObj.getJSONArray("tag");
            for (int i = 0; i < array.length(); i++) {
                itemVOS.add(new VendorCatItemVO((JSONObject) array.get(i)));

            }

            adapter = new VendorCatItemListAdapter(getContext(), itemVOS, this);
            recyclerView.setAdapter(adapter);
            if (itemVOS == null) {
                notFoundTv.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                notFoundTv.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(VolleyError error, int requestTag) {
        progressBar.setVisibility(View.GONE);
        notFoundTv.setVisibility(View.VISIBLE);
        Log.e("error", error.toString());
    }

    @Override
    public void onGetItem(VendorCatItemVO itemVO) {
       /* getArguments().putSerializable("itemVO", itemVO);
        ((VendorItemListActivity) getContext()).loadFragment(Constants.Fragment.ADD_ITEM, getArguments(), Constants.FragTransaction.ADD);*/

    }

    @Override
    public String getToolbarTitle() {
        return vendorVO != null ? vendorVO.getVendorTitle() : getString(R.string.app_name);
    }
}
