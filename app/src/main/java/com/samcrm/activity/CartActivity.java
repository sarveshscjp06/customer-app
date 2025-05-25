package com.samcrm.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.samcrm.Constants;
import com.samcrm.R;
import com.samcrm.activity_manager.BaseActivity;

public class CartActivity extends BaseActivity {
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        loadFragment(Constants.Fragment.CART, null, Constants.FragTransaction.REPLACE);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_cart;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.inflateMenu(R.menu.cart);
        return true;
    }
}









