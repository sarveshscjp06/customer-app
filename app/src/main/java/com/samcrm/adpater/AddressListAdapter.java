package com.samcrm.adpater;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.samcrm.R;
import com.samcrm.vo.ShippingAddressVO;

import java.util.List;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.MyViewHolder> {
    private Context context;
    private List<ShippingAddressVO> shippingAddressVOS;
    private OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, addressOne, addressTwo, addressThree;
        private TextView mobile;


        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            addressOne = view.findViewById(R.id.address_one);
            addressTwo = view.findViewById(R.id.address_two);
            addressThree = view.findViewById(R.id.address_three);
            mobile = view.findViewById(R.id.mobile);


        }
    }


    public AddressListAdapter(Context context, List<ShippingAddressVO> shippingAddressVOS, OnItemClickListener listener) {
        this.context = context;
        this.shippingAddressVOS = shippingAddressVOS;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_address_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ShippingAddressVO item = shippingAddressVOS.get(position);
        holder.name.setText(item.getName());
        holder.mobile.setText(item.getMobile());
        holder.addressOne.setText(item.getAddress());
        holder.addressTwo.setText(item.getZipcode());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGetShippingAddress(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shippingAddressVOS.size();
    }

    public interface OnItemClickListener {
        void onGetShippingAddress(ShippingAddressVO ShippingAddressVO);

    }

}

