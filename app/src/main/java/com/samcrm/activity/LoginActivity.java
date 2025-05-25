package com.samcrm.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.samcrm.R;
import com.samcrm.Constants;
import com.samcrm.activity_manager.BaseActivity;

import com.samcrm.network.NetworkConstant;
import com.samcrm.network.NetworkRequester;
import com.samcrm.utility.SharedPref;
import com.samcrm.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {
    private int generatedOtp = 0;
    private String TAG = LoginActivity.class.getSimpleName();
    private EditText mMobileView;
    private EditText mPasswordView;
    private Button register;
    private Toolbar toolbar;
    private Button mEmailSignInButton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        mMobileView = findViewById(R.id.mobile);

        mPasswordView = findViewById(R.id.password);


        mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                attemptLogin();

            }
        });
        register = findViewById(R.id.registerBtn);
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(newIntent);

            }
        });
    }


    private void attemptLogin() {
        mMobileView.setError(null);
        mPasswordView.setError(null);

        String mobile = mMobileView.getText().toString();

        String password = mPasswordView.getText().toString();


        boolean cancel = false;
        View focusView = null;

        // Check for a valid input.
        if (TextUtils.isEmpty(mobile)) {
            mMobileView.setError(getString(R.string.error_field_required));
            focusView = mMobileView;
            cancel = true;
        } else if (!isMobileValid(mobile)) {
            mMobileView.setError(getString(R.string.error_invalid_mobile));
            focusView = mMobileView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showDialog();
            String tag_json_obj = "json_user_authentication_req";
            HashMap<String, String> params = new HashMap<>();
            params.put("ipAddress", Utility.getDeviceImei(this));
            params.put("mobile", mobile);
            params.put("password", password);
            params.put("individual_id", "");
            params.put("name", "");
            params.put("email", "");
            params.put("zipCode", "");
            params.put("vendorTitle", "");
            params.put("vendorRegistrationNo", "");
            params.put("vendorDescription", "");
            params.put("vendorCategoryId", "");
            params.put("vendorSubtypeId", "");
            params.put("imageName", "");
            params.put("imageUrl", "");
            params.put("imageBitmapString", "");
            params.put("mobileVerificationCode", "");

            JSONObject reqParams = Utility.getRequestParam(LoginActivity.this, null);
            Utility.addParamJson(reqParams, new JSONObject(params));
            new NetworkRequester.RequestBuilder(this)
                    .setRequestType(NetworkConstant.ReqType.POST)
                    .setUrl(NetworkConstant.loginURL)
                    .setListener(this)
                    .setReqParams(reqParams)
                    .setRequestTag(NetworkConstant.RequestTagType.login)
                    .build()
                    .sendRequest();
        }
    }


    private boolean isPasswordValid(String password) {
        return password.length() > 4 && password.length() <= 10;
    }

    private boolean isMobileValid(String mobile) {
        return mobile.length() <= 10;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }


    @Override
    public void onResponse(Object data, int requestTag){

        hideDialog();
        Log.d(TAG, data.toString());
        try {
            JSONObject jsonObj = new JSONObject(data.toString());
            // When the JSON response has status boolean value as samcrmAuthentication signed with true
            if (jsonObj.getBoolean("status")) {
                generatedOtp = 0;
                Toast.makeText(getApplicationContext(), "User registrantion / authentication / updation successful!", Toast.LENGTH_LONG).show();
                JSONArray vendorsList = jsonObj.getJSONArray("tag");
                JSONObject object = new JSONObject();
                object.put("status", new JSONObject(jsonObj.getString("errorMessage")));

                Intent isService = new Intent(getApplicationContext(), LoginActivity.class);
                stopService(isService);
                SharedPref.getInstance(LoginActivity.this).saveString(Constants.SharePrefName.USER_DETAILS, object.toString());
                Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // newIntent.putExtra("UserProfile", object.toString());
                startActivity(newIntent);
            } else if (!jsonObj.getString("errorMessage").isEmpty()) {
                if (jsonObj.getString("errorMessage").equalsIgnoreCase("User Registration Failed")) {
                    Toast.makeText(getApplicationContext(), "Vendor / Customer Registration Failed.", Toast.LENGTH_LONG).show();
                } else if (jsonObj.getString("errorMessage").equalsIgnoreCase("SAM-CRM login: mobile-no OR password is wrong.")) {
                    Toast.makeText(getApplicationContext(), "SAM-CRM login: mobile-no OR password is wrong.", Toast.LENGTH_LONG).show();
                } else {
                    generatedOtp = jsonObj.getInt("errorMessage");
                    //mOtpView.setError("Enter OTP which is sent to your mobile number. \nAnd click the register button again!");
                    // mOtpView.requestFocus();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Something Wrongly Entered.", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(VolleyError error, int requestTag) {
        hideDialog();
        VolleyLog.d(TAG, "Error: " + error.getMessage());
    }
}

