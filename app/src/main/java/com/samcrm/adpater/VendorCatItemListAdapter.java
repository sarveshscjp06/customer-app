package com.samcrm.adpater;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.VolleyError;
import com.samcrm.Constants;
import com.samcrm.R;
import com.samcrm.network.NetworkConstant;
import com.samcrm.network.NetworkRequester;
import com.samcrm.network.NetworkResponseListener;
import com.samcrm.utility.SharedPref;
import com.samcrm.utility.Utility;
import com.samcrm.vo.VendorCatItemVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VendorCatItemListAdapter extends RecyclerView.Adapter<VendorCatItemListAdapter.MyViewHolder> implements NetworkResponseListener {
    private Context context;
    private List<VendorCatItemVO> itemVOS;
    private OnItemClickListener listener;
    private int minteger = 0;
    private VendorCatItemVO vendorCatItemVO;
    private int count;
    public ProgressDialog progressDialog;


    public VendorCatItemListAdapter(Context context, List<VendorCatItemVO> itemVOS, OnItemClickListener listener) {
        this.context = context;
        this.itemVOS = itemVOS;
        this.listener = listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final VendorCatItemVO item = itemVOS.get(position);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        holder.name.setText(item.getProductName());
        holder.description.setText(item.getProductDescription());
        holder.orderPrice.setText(item.getOfferPrice());
        if (item.getQuantity() == 0) {
            holder.add.setEnabled(false);
            holder.add.setText("out of stock");
        } else {
            holder.add.setEnabled(true);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGetItem(item);
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  holder.countTable.setVisibility(View.VISIBLE);
              //  holder.addTable.setVisibility(View.GONE);
              //  increaseInteger(holder.itemCount, item);
                requestForCheckInventory(1,item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemVOS.size();
    }


    public interface OnItemClickListener {
        void onGetItem(VendorCatItemVO itemVO);

    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, orderPrice, description, itemCount;
        private Button decrease, increase, add;
        private TableRow countTable, addTable;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            orderPrice = view.findViewById(R.id.orderPrice);
            decrease = view.findViewById(R.id.decrease);
            increase = view.findViewById(R.id.increase);
            itemCount = view.findViewById(R.id.integer_number);
            countTable = view.findViewById(R.id.countTable);
            addTable = view.findViewById(R.id.addTable);
            add = view.findViewById(R.id.add);
        }
    }

    private void requestForCheckInventory(int count, VendorCatItemVO item) {
        this.vendorCatItemVO = item;
        progressDialog.show();
        JSONObject reqParams = Utility.getRequestParam(context, null);
        new NetworkRequester.RequestBuilder(context)
                .setRequestType(NetworkConstant.ReqType.GET)
                .setUrl(NetworkConstant.samcrmCheckInventory
                        + "?productId=" + item.getProductId() + "&vendorId=" + Utility.getVendorId(context))
                .setListener(this)
                .setReqParams(reqParams)
                .setRequestTag(NetworkConstant.RequestTagType.samcrmCheckInventory)
                .build()
                .sendRequest();
    }

    @Override
    public void onResponse(Object data, int requestTag) {
        Log.e("dsd", data.toString());
        progressDialog.cancel();

        switch (requestTag) {
            case NetworkConstant.RequestTagType.samcrmBookOrder:
                Utility.t(context, "Successfully Added");

                try {
                    JSONObject jsonObj = new JSONObject(data.toString());
                    boolean errorMessage = jsonObj.getBoolean("errorMessage");
                    if(errorMessage) {
                        JSONArray array = jsonObj.getJSONArray("tag");
                     JSONObject jsonObject =  (JSONObject)array.get(0);
                        JSONArray items = jsonObject.getJSONArray("items");
                        Utility.clearCartCount(context);
                        Utility.setCartCount(context,items.length());
                        Utility.t(context, "Successfully Added");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case NetworkConstant.RequestTagType.samcrmCheckInventory:
                Log.e("response", data.toString());
                try {
                    JSONObject jsonObj = new JSONObject(data.toString());
                    if (jsonObj.getString("errorMessage") == "0") {
                        Utility.t(context, "Out of stock");
                    } else {
                        bookOrderRequest(count, vendorCatItemVO);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    public void onError(VolleyError error, int requestTag) {
        Log.e("error", error.toString());
        progressDialog.cancel();

    }

    private void bookOrderRequest(int count, VendorCatItemVO vendorVO) {
        progressDialog.show();
        JSONObject js = new JSONObject();
        try {
            js.put("vendorId",1);// Utility.getVendorId(context));
            js.put("customerId",9); //Utility.getCustomerID(context));
            js.put("orderId",0); //getOrderId // 1 //o
            js.put("addressId", 0);
            js.put("status", "");
            js.put("deliveryDate", "");
            js.put("deliveryTime", "");
            js.put("deliveryPerson", "");
            js.put("deliveryAddress", "primary");
            js.put("price", "");
            js.put("comments", "");
            JSONArray items = new JSONArray();
            items.put(Integer.parseInt(vendorVO.getProductId()));
            js.put("items", items);

            JSONArray counts = new JSONArray();
            counts.put(count);
            js.put("counts", counts);
            Log.e("json", js.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject reqParams = Utility.getRequestParam(context, null);
        Utility.addParamJson(reqParams, js);
        new NetworkRequester.RequestBuilder(context)
                .setRequestType(NetworkConstant.ReqType.POST)
                .setUrl(NetworkConstant.samcrmBookOrder)
                .setListener(this)
                .setReqParams(reqParams)
                .setRequestTag(NetworkConstant.RequestTagType.samcrmBookOrder)
                .build()
                .sendRequest();

    }

}

