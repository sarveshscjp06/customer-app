package com.samcrm.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samcrm.R;
import com.samcrm.vo.CartVO;
import com.samcrm.vo.VendorVO;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {
    private Context context;
    private List<CartVO> vendorVOS;
    private OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name ,details;
        private TextView description;


        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            details  = view.findViewById(R.id.details);

        }
    }


    public OrdersAdapter(Context context, List<CartVO> productVOS, OnItemClickListener listener) {
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
        final CartVO item = vendorVOS.get(position);
        holder.name.setText(item.getDelivery_date());
        holder.description.setText("Order Id = "+item.getOrder_id());
        holder.details.setOnClickListener(new View.OnClickListener() {
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
        void onGetItemClick(CartVO productVO);

    }

}

