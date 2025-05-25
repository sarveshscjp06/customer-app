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
import com.samcrm.adpater.OrderDetailsAdapter;
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

public class OrderDetailsFragment extends BaseFragment implements NetworkResponseListener, OrderDetailsAdapter.OnItemClickListener {

    private OrderDetailsAdapter adapter;
    private ArrayList<CartVO> itemVOS;
    private RecyclerView recyclerView;
    private TextView notFoundTv;
    private ProgressBar progressBar;
    private CartVO cartVO;
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
            cartVO = (CartVO) getArguments().getSerializable("cartVO");
            if (cartVO.getItemCartVOS().size()!=0) {
                adapter = new OrderDetailsAdapter(getContext(), getFilterList(), cartVO, this);
                recyclerView.setAdapter(adapter);
            }
            if (cartVO.getItemCartVOS().size()==0) {
                notFoundTv.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                notFoundTv.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }




    private HashMap<String, ArrayList> getFilterList() {
        HashMap<String, ArrayList> filterMap = new HashMap<>();
        filterMap.put("Item Details", cartVO.getItemCartVOS());
        return filterMap;
    }





    @Override
    public String getToolbarTitle() {
        return "Order Details";
    }


    @Override
    public void cancelOrder(CartVO itemVO) {

    }

    @Override
    public void orderStatus(CartVO itemVO) {

    }
}
