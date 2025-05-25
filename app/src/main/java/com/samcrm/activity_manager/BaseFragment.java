package com.samcrm.activity_manager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.VolleyError;
import com.samcrm.network.NetworkResponseListener;


public class BaseFragment extends Fragment implements NetworkResponseListener, View.OnClickListener, DialogInterface.OnClickListener {
    public Context context;
    public int fragCode = -1;
    private String fragmentTag;
    public FragmentInteractionListener fragListener;
    public ProgressDialog progressDialog;
    public boolean isActivityCreated;


    public BaseFragment() {

    }

    public String getFragmentTag() {
        return fragmentTag;
    }

    public void setFragmentTag(String fragmentTag) {
        this.fragmentTag = fragmentTag;
    }

    public int getFragCode() {
        return fragCode;
    }

    public void setFragCode(int fragCode) {
        this.fragCode = fragCode;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof FragmentInteractionListener) {
            fragListener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentInteractionListener");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

    }


    @Override
    public void onResume() {
        super.onResume();
        fragListener.setupToolbar(getArguments(), this, getToolbarTitle());
        fragListener.setToolbarScroll(getScrollToolBar());
    }


    private boolean getScrollToolBar() {
        return true;
    }

    public String getToolbarTitle() {
        Bundle bundle = getArguments();
        String title = bundle.getString("title");
        return title;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragListener = null;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    public boolean onBackEvent() {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public interface FragmentInteractionListener {
        void loadFragment(int fragmentId, Bundle bundle, int transactionMode);

        void setupToolbar(Bundle bundle, Fragment fragment, String toolbarTitle);


        void setTransitAnimationFlag(boolean isTrasniting);

        void setToolbarScroll(boolean isScroll);

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation anim = super.onCreateAnimation(transit, enter, nextAnim);
        //If not, and an animation is defined, load it now
        if (anim == null && nextAnim != 0) {
            anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        }
        if (anim != null) {
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    fragListener.setTransitAnimationFlag(true);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    fragListener.setTransitAnimationFlag(false);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        return anim;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onError(VolleyError error, int requestTag) {

    }

    @Override
    public void onResponse(Object data, int requestTag) {

    }
}

