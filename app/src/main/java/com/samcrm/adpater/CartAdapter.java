package com.samcrm.adpater;

import android.content.Context;
import android.os.Bundle;
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
import com.samcrm.activity_manager.BaseActivity;
import com.samcrm.fragment.CartFragment;
import com.samcrm.network.NetworkConstant;
import com.samcrm.network.NetworkRequester;
import com.samcrm.network.NetworkResponseListener;
import com.samcrm.utility.Utility;
import com.samcrm.vo.CartVO;
import com.samcrm.vo.ItemCartVO;
import com.samcrm.vo.SearchDataFilterVO;
import com.samcrm.vo.VendorCatItemVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;
    private Context context;
    private CartVO cartVO;
    private OnItemClickListener listener;
    private ArrayList<SearchDataFilterVO> dataList;
    private HashMap<String, ArrayList> filterMap;


    public CartAdapter(Context context, HashMap<String, ArrayList> filterList, CartVO cartVO, OnItemClickListener listener) {
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
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_item, parent, false);
            return new MyViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_header, parent, false);
            return new HeaderViewHolder(itemView);
        } else if (viewType == TYPE_FOOTER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_footer, parent, false);
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
            footerHolder.totalDiscounts.setText("₹ " + new ItemCartVO().totalDis(cartVO.getItemCartVOS()));
            footerHolder.totalPayout.setText("₹ " + (new ItemCartVO().totalPrice(cartVO.getItemCartVOS()) - new ItemCartVO().totalDis(cartVO.getItemCartVOS())));
            footerHolder.totalPrice.setText("₹ " + new ItemCartVO().totalPrice(cartVO.getItemCartVOS()));
            footerHolder.proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  if (!new CartVO().isOutOfStock(cartList)) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("cartVO", cartVO);
                    ((BaseActivity) context).loadFragment(Constants.Fragment.CHECKOUT, bundle, Constants.FragTransaction.ADD);

                    //  } else {
                    //     Utility.t(context, "Please Remove out of stock product ");
                    //  }*/

                }
            });

        } else if (holder instanceof MyViewHolder) {
            final MyViewHolder itemViewHolder = (MyViewHolder) holder;
            searchDataFilterVO = dataList.get(position);
            final ItemCartVO item = (ItemCartVO) searchDataFilterVO.getDataObj();
            itemViewHolder.name.setText(item.getProduct_name());
            itemViewHolder.description.setText(item.getDescription());
            itemViewHolder.orderPrice.setText("₹ " + item.getPrice());
            itemViewHolder.itemCount.setText("" + item.getQuantity());


            itemViewHolder.decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    decreaseInteger(itemViewHolder.itemCount, item);
                }
            });
            itemViewHolder.increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    increaseInteger(itemViewHolder.itemCount, item);
                }
            });

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
        void removeItem(ItemCartVO itemVO);
        void quantityUpdate(int number, ItemCartVO itemVO);
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

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;

        public HeaderViewHolder(View view) {
            super(view);
            headerTitle = (TextView) view.findViewById(R.id.header_text);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        private TextView totalPrice;
        private TextView totalDiscounts;
        private TextView totalPayout;
        private Button proceed;

        public FooterViewHolder(View view) {
            super(view);
            proceed = view.findViewById(R.id.submit);
            totalDiscounts = view.findViewById(R.id.Discounts);
            totalPayout = view.findViewById(R.id.payout);
            totalPrice = view.findViewById(R.id.totalPrice);

        }
    }


    public void decreaseInteger(TextView view, ItemCartVO item) {
        if (item.getQuantity() > 1) {
          int  minteger = item.getQuantity() - 1;
          display(minteger, view, item);

        } else {
            display(1, view, item);


       }
    }

    private void display(int number, TextView displayInteger, ItemCartVO item) {
        displayInteger.setText("" + number);
        listener.quantityUpdate(number, item);
    }

    public void increaseInteger(TextView view, ItemCartVO item) {
        //if (minteger < item.getQuantity()) {
         int   minteger = item.getQuantity() + 1;
            display(minteger, view, item);

        //} else {
          //  Toast.makeText(context, "max", Toast.LENGTH_LONG).show();
       // }
    }

}

