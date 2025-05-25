package com.samcrm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.samcrm.R;
import com.samcrm.activity.MainActivity;
import com.samcrm.activity_manager.BaseFragment;
import com.samcrm.utility.Utility;
import com.samcrm.vo.CartVO;
import com.samcrm.vo.ItemCartVO;

public class OrderSuccessFragment extends BaseFragment {
    private Button home;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order_sucees, container, false);
        bindView(rootView);
        return rootView;
    }

    private void bindView(View view) {
        home = view.findViewById(R.id.submit);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearCart();
            }
        });
    }

    @Override
    public String getToolbarTitle() {
        return getString(R.string.app_name);
    }

    public void ClearCart() {
        Utility.clearCartCount(context);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cart, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                ClearCart();


        }

        return false;
    }
}
