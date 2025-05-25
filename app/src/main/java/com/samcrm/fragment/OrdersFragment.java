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
import com.samcrm.Constants;
import com.samcrm.R;
import com.samcrm.activity_manager.BaseActivity;
import com.samcrm.activity_manager.BaseFragment;
import com.samcrm.adpater.CartAdapter;
import com.samcrm.adpater.OrdersAdapter;
import com.samcrm.network.NetworkConstant;
import com.samcrm.network.NetworkRequester;
import com.samcrm.network.NetworkResponseListener;
import com.samcrm.utility.Utility;
import com.samcrm.vo.CartVO;
import com.samcrm.vo.ItemCartVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OrdersFragment extends BaseFragment implements NetworkResponseListener, OrdersAdapter.OnItemClickListener {

    private OrdersAdapter adapter;
    private ArrayList<CartVO> itemVOS;

    private RecyclerView recyclerView;
    private TextView notFoundTv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item, container, false);
        bindView(rootView);
        return rootView;
    }

    private void bindView(View rootView) {
        notFoundTv = rootView.findViewById(R.id.no_data);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        bookOrderRequest();

    }

    private void bookOrderRequest() {
        progressDialog.show();
        JSONObject reqParams = Utility.getRequestParam(context, null);
        new NetworkRequester.RequestBuilder(context)
                .setRequestType(NetworkConstant.ReqType.GET)
                .setUrl(NetworkConstant.samcrmOrdersList + "?mobile=9415382922&customerId=9&status=confirmed")
                .setListener(this)
                .setReqParams(reqParams)
                .setRequestTag(NetworkConstant.RequestTagType.samcrmOrdersList)
                .build()
                .sendRequest();


    }

    @Override
    public void onResponse(Object data, int requestTag) {
        progressDialog.hide();
        Log.e("response", data.toString());
        switch (requestTag) {
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
                        adapter = new OrdersAdapter(getContext(), itemVOS, this);
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


    @Override
    public void onError(VolleyError error, int requestTag) {
        progressDialog.hide();
        notFoundTv.setVisibility(View.VISIBLE);
        Log.e("error", error.toString());
    }


    @Override
    public String getToolbarTitle() {
        return "My Orders";
    }


    @Override
    public void onGetItemClick(CartVO productVO) {
        getArguments().putSerializable("cartVO", productVO);
        ((BaseActivity) context).loadFragment(Constants.Fragment.Orders_details, getArguments(), Constants.FragTransaction.ADD);

    }
}
