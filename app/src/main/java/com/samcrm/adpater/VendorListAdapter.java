package com.samcrm.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samcrm.R;
import com.samcrm.vo.VendorVO;


import java.util.List;

public class VendorListAdapter extends RecyclerView.Adapter<VendorListAdapter.MyViewHolder> {
    private Context context;
    private List<VendorVO> vendorVOS;
    private OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView description;


        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);

        }
    }


    public VendorListAdapter(Context context, List<VendorVO> productVOS, OnItemClickListener listener) {
        this.context = context;
        this.vendorVOS = productVOS;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final VendorVO item = vendorVOS.get(position);
        holder.name.setText(item.getVendorTitle());
        holder.description.setText(item.getVendorCategoryDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGetItemClick(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return vendorVOS.size();
    }

    public interface OnItemClickListener {
        void onGetItemClick(VendorVO productVO);

    }

}

