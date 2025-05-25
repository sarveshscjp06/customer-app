package com.samcrm.activity_manager;

import android.app.ProgressDialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.samcrm.R;
import com.samcrm.Constants;

import com.samcrm.activity.CartActivity;
import com.samcrm.activity.MainActivity;
import com.samcrm.network.NetworkResponseListener;
import com.samcrm.utility.Utility;


public abstract class BaseActivity extends AppCompatActivity implements NetworkResponseListener, BaseFragment.FragmentInteractionListener, View.OnClickListener {
    public BaseFragment loadedFragment;
    public Toolbar toolbar;
    public boolean IS_TRANSIT_ANIMATION;
    public ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        configureToolbar();


    }


    private void configureToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected abstract int getLayoutResource();

    @Override
    public void loadFragment(int fragmentId, Bundle bundle, int transactionMode) {

        if (transactionMode != Constants.FragTransaction.NONE) {
            loadedFragment = FragmentFactory.getInstance(fragmentId, bundle, loadedFragment);
            if (loadedFragment == null) {
                return;
            }
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exits_to_left, R.anim.enter_from_left, R.anim.exits_to_right);

        switch (transactionMode) {
            case Constants.FragTransaction.REPLACE:
                fragmentTransaction.replace(R.id.fragmentContainer, loadedFragment);
                // fragmentTransaction.addToBackStack(loadedFragment.getFragmentTag());
                break;
            case Constants.FragTransaction.ADD:
                fragmentTransaction.replace(R.id.fragmentContainer, loadedFragment, loadedFragment.getFragmentTag());
                fragmentTransaction.addToBackStack(loadedFragment.getFragmentTag());
                break;
            case Constants.FragTransaction.NONE:
                return;
        }

        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
        invalidateOptionsMenu();
    }

    @Override
    public void setupToolbar(Bundle bundle, Fragment fragment, String toolbarTitle) {
        loadedFragment = (BaseFragment) fragment;
        int id = bundle.getInt("fragCode");
        toolbar.setTitle(toolbarTitle);


    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void setToolbarScroll(boolean isScroll) {

    }

    @Override
    public void onBackPressed() {
        if (IS_TRANSIT_ANIMATION) {
            return;
        }
        if (loadedFragment != null) {
            if (loadedFragment.onBackEvent()) {
                return;
            }

            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {


                if (this instanceof CartActivity) {

                    switch (loadedFragment.getFragCode()) {
                        case Constants.Fragment.ORDER_SUCCESS:
                            Utility.clearCartCount(BaseActivity.this);
                            Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                            break;
                        default:
                            backFragmentState();
                    }


                } else {

                    switch (loadedFragment.getFragCode()) {
                        default:
                            backFragmentState();
                    }


                }
            } else {
                finish();
            }

        } else {
            finish();
        }
    }


    @Override
    public void setTransitAnimationFlag(boolean isTransiting) {
        this.IS_TRANSIT_ANIMATION = isTransiting;
    }

    private void backFragmentState() {
        getSupportFragmentManager().popBackStack();
        invalidateOptionsMenu();
    }

    public void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    public void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onResponse(Object data, int requestTag) {
        Log.e("response", data.toString());
    }

    @Override
    public void onError(VolleyError error, int requestTag) {

    }
}