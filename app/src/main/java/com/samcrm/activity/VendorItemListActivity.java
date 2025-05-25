package com.samcrm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.samcrm.R;
import com.samcrm.Constants;
import com.samcrm.activity_manager.BaseActivity;
import com.samcrm.utility.Utility;

public class VendorItemListActivity extends BaseActivity {
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        loadFragment(Constants.Fragment.VENDOR_ITEM_LIST, bundle, Constants.FragTransaction.REPLACE);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_vendor_items;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;
            case R.id.cart:
                Intent newIntent = new Intent(getApplicationContext(), VendorItemListActivity.class);
                startActivity(newIntent);
                // return true;
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.inflateMenu(R.menu.menu_cart);
        MenuItem cartMenuItem = toolbar.getMenu().findItem(R.id.cart);
        MenuItemCompat.setActionView(cartMenuItem, R.layout.menu_cart_badge);
        Utility.updateCartCounterUI(toolbar,this);
        final FrameLayout cartActionView = (FrameLayout) MenuItemCompat.getActionView(cartMenuItem);
        ImageView container = cartActionView.findViewById(R.id.cart_btn);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.  onCartMenuItemClicked(VendorItemListActivity.this);
            }
        });
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.updateCartCounterUI(toolbar,this);
    }
}









