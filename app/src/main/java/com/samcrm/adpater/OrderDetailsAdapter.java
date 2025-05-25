package com.samcrm.adpater;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.samcrm.Constants;
import com.samcrm.R;
import com.samcrm.activity_manager.BaseActivity;
import com.samcrm.vo.CartVO;
import com.samcrm.vo.ItemCartVO;
import com.samcrm.vo.SearchDataFilterVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class OrderDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;
    private Context context;
    private CartVO cartVO;
    private OnItemClickListener listener;
    private ArrayList<SearchDataFilterVO> dataList;
    private HashMap<String, ArrayList> filterMap;


    public OrderDetailsAdapter(Context context, HashMap<String, ArrayList> filterList, CartVO cartVO, OnItemClickListener listener) {
        this.context = context;
        this.filterMap = filterList;
        this.cartVO = cartVO;
        this.listener = listener;
        prepareDataList();
    }


    private void prepareDataList() {
        this.dataList = new ArrayList();
        Set<Map.Entry<String, ArrayList>> entrySet = this.filterMap.entrySet();
        Iterator<Map.Entry<String, ArrayList>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList> entry = iterator.next();
            dataList.add(getFilterVO(entry.getKey()));
            for (Object obj : entry.getValue()) {
                dataList.add(getFilterVO(obj));
            }
        }
    }

    private SearchDataFilterVO getFilterVO(Object obj) {
        return new SearchDataFilterVO(obj);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_list_item, parent, false);
            return new MyViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_header, parent, false);
            return new HeaderViewHolder(itemView);
        } else if (viewType == TYPE_FOOTER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_footer, parent, false);
            return new FooterViewHolder(itemView);
        } else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SearchDataFilterVO searchDataFilterVO;
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            searchDataFilterVO = dataList.get(position);
            headerHolder.headerTitle.setText((String) searchDataFilterVO.getDataObj());

        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;

            footerHolder.name.setText(cartVO.getDelivery_person());
            footerHolder.mobile.setText("" + cartVO.getMobile());
            footerHolder.addressOne.setText(cartVO.getStreet());
            footerHolder.addressTwo.setText(cartVO.getCountry());
            footerHolder.addressThree.setText(cartVO.getZipcode());

            footerHolder.totalPrice.setText("₹ " + new ItemCartVO().totalPrice(cartVO.getItemCartVOS()));
            footerHolder.cancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.cancelOrder(cartVO);
                }
            });
            footerHolder.orderStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.orderStatus(cartVO);
                }
            });

        } else if (holder instanceof MyViewHolder) {
            final MyViewHolder itemViewHolder = (MyViewHolder) holder;
            searchDataFilterVO = dataList.get(position);
            final ItemCartVO item = (ItemCartVO) searchDataFilterVO.getDataObj();
            itemViewHolder.name.setText(item.getProduct_name());
            itemViewHolder.description.setText(item.getDescription());
            itemViewHolder.orderPrice.setText("₹ " + item.getPrice());



        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == dataList.size()) {
            return TYPE_FOOTER;
        } else {
            SearchDataFilterVO searchDataFilterVO = dataList.get(position);
            Object dataObj = searchDataFilterVO.getDataObj();
            if (dataObj instanceof ItemCartVO) {
                return TYPE_ITEM;
            } else if (dataObj instanceof String) {
                return TYPE_HEADER;
            }
        }
        return -1;
    }

    public interface OnItemClickListener {
        void cancelOrder(CartVO itemVO);

        void orderStatus(CartVO itemVO);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, orderPrice, description;


        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            orderPrice = view.findViewById(R.id.orderPrice);


        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;

        public HeaderViewHolder(View view) {
            super(view);
            headerTitle = (TextView) view.findViewById(R.id.header_text);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        private TextView totalPrice;

        private Button cancelOrder, orderStatus;
        private TextView name, addressOne, addressTwo, addressThree, mobile;

        public FooterViewHolder(View view) {
            super(view);

            totalPrice = view.findViewById(R.id.totalPrice);
            name = view.findViewById(R.id.name);
            addressOne = view.findViewById(R.id.address_one);
            addressTwo = view.findViewById(R.id.address_two);
            addressThree = view.findViewById(R.id.address_three);
            mobile = view.findViewById(R.id.mobile);
            orderStatus = view.findViewById(R.id.order_status);
            cancelOrder = view.findViewById(R.id.cancel_order);

        }
    }


}

