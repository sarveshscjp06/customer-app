package com.samcrm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;


import com.android.volley.VolleyError;

import com.samcrm.R;
import com.samcrm.Constants;
import com.samcrm.activity_manager.BaseActivity;

import com.samcrm.network.NetworkConstant;
import com.samcrm.network.NetworkRequester;
import com.samcrm.utility.SharedPref;
import com.samcrm.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class SplashActivity extends BaseActivity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (SharedPref.getInstance(getApplicationContext()).getString(Constants.SharePrefName.USER_DETAILS) != null) {
                    Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(newIntent);

                } else {
                    checkSamcrmCredentials();
                }


            }
        }, SPLASH_TIME_OUT);
    }

    private void checkSamcrmCredentials() {
        String tag_json_obj = "json_user_data_req";
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", "");
        params.put("ipAddress", Utility.getDeviceImei(this));
        JSONObject reqParams = Utility.getRequestParam(SplashActivity.this, null);
        Utility.addParamJson(reqParams, new JSONObject(params));

        new NetworkRequester.RequestBuilder(this)
                .setRequestType(NetworkConstant.ReqType.POST)
                .setUrl(NetworkConstant.userDataURL)
                .setListener(this)
                .setReqParams(reqParams)
                .setRequestTag(NetworkConstant.RequestTagType.userDataURL)
                .build()
                .sendRequest();


    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    public void onResponse(Object data, int requestTag){

        try {
            JSONObject jsonObj = new JSONObject(data.toString());
            if (jsonObj.getBoolean("errorMessage")) {
                JSONObject object = new JSONObject();
                object.put("status", new JSONObject(jsonObj.getString("status")));
                Toast.makeText(getApplicationContext(), "You are already logged-in!", Toast.LENGTH_LONG).show();
                SharedPref.getInstance(SplashActivity.this).saveString(Constants.SharePrefName.USER_DETAILS, object.toString());
                Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(newIntent);
            } else {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(VolleyError error, int requestTag) {
        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}