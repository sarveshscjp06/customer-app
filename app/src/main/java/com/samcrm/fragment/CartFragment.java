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
import com.samcrm.adpater.CartAdapter;
import com.samcrm.adpater.VendorCatItemListAdapter;
import com.samcrm.network.NetworkConstant;
import com.samcrm.network.NetworkRequester;
import com.samcrm.network.NetworkResponseListener;
import com.samcrm.utility.Utility;
import com.samcrm.vo.CartVO;
import com.samcrm.vo.ItemCartVO;
import com.samcrm.vo.VendorCatItemVO;
import com.samcrm.vo.VendorCategoryVO;
import com.samcrm.vo.VendorVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartFragment extends BaseFragment implements NetworkResponseListener, CartAdapter.OnItemClickListener {

    private CartAdapter adapter;
    private ArrayList<CartVO> itemVOS;

    private RecyclerView recyclerView;
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
        bookOrderRequest();

    }

    private void bookOrderRequest() {

        JSONObject reqParams = Utility.getRequestParam(context, null);
        new NetworkRequester.RequestBuilder(context)
                .setRequestType(NetworkConstant.ReqType.GET)
                .setUrl(NetworkConstant.samcrmOrdersList + "?mobile=9415382922&customerId=9&status=open")
                .setListener(this)
                .setReqParams(reqParams)
                .setRequestTag(NetworkConstant.RequestTagType.samcrmOrdersList)
                .build()
                .sendRequest();


    }

    @Override
    public void onResponse(Object data, int requestTag) {
        progressBar.setVisibility(View.GONE);
        Log.e("response", data.toString());
        switch (requestTag) {
            case NetworkConstant.RequestTagType.samcrmCheckInventoryAndUpdateQuantity:
                bookOrderRequest();
                Utility.t(context, "Successfully remove ");
                break;

            case NetworkConstant.RequestTagType.samcrmOrdersList:
                try {
                    itemVOS = new ArrayList<>();
                    itemVOS.clear();
                    JSONObject jsonObj = new JSONObject(data.toString());
                    JSONArray array = jsonObj.getJSONArray("tag");
                    for (int i = 0; i < array.length(); i++) {
                        itemVOS.add(new CartVO((JSONObject) array.get(i)));

                    }
                    if (itemVOS.size() != 0) {
                        adapter = new CartAdapter(getContext(), getFilterList(), itemVOS.get(0), this);
                        recyclerView.setAdapter(adapter);
                    }
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
                break;
        }
    }

    private HashMap<String, ArrayList> getFilterList() {
        HashMap<String, ArrayList> filterMap = new HashMap<>();
        filterMap.put("Item Details", itemVOS.get(0).getItemCartVOS());
        return filterMap;
    }

    @Override
    public void onError(VolleyError error, int requestTag) {
        progressBar.setVisibility(View.GONE);
        notFoundTv.setVisibility(View.VISIBLE);
        Log.e("error", error.toString());
    }

    private void CheckInventoryAndUpdateQuantityRequest(int count, ItemCartVO itemCartVO) {
        JSONObject reqParams = Utility.getRequestParam(context, null);
        new NetworkRequester.RequestBuilder(context)
                .setRequestType(NetworkConstant.ReqType.GET)
                .setUrl(NetworkConstant.samcrmCheckInventoryAndUpdateQuantity
                        + "?productId=" + itemCartVO.getProduct_id()
                        + "&orderId=" + itemCartVO.getOrder_id()
                        + "&quantity" + count
                        + "&itemId" + itemCartVO.getItem_id())
                .setListener(this)
                .setReqParams(reqParams)
                .setRequestTag(NetworkConstant.RequestTagType.samcrmCheckInventoryAndUpdateQuantity)
                .build()
                .sendRequest();
    }

    @Override
    public String getToolbarTitle() {
        return getString(R.string.app_name);
    }

    @Override
    public void removeItem(ItemCartVO itemVO) {

    }

    @Override
    public void quantityUpdate(int number, ItemCartVO itemVO) {
        CheckInventoryAndUpdateQuantityRequest(number, itemVO);
    }
}
