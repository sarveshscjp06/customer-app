package com.samcrm.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.samcrm.R;
import com.samcrm.Constants;
import com.samcrm.activity_manager.BaseActivity;
import com.samcrm.network.AppController;
import com.samcrm.network.NetworkConstant;
import com.samcrm.utility.SharedPref;
import com.samcrm.utility.Utility;

import com.samcrm.network.NetworkRequester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends BaseActivity {

    private String TAG = RegisterActivity.class.getSimpleName();
    private Utility commonUtility = new Utility();
    private int generatedOtp = 0;
    private String individual_id;

    // UI references.
    private EditText mNameView;
    private EditText mEmailView;
    private EditText mMobileView;
    private EditText mZipCodeView;
    private EditText mPasswordView;
    private CheckBox mAreYouVendor;
    private EditText mVendorTitle;
    private EditText mVendorRegistrationNo;
    private EditText mVendorDescription;
    private Spinner mVendorId, mSubtypeId;
    @SuppressLint("UseSparseArrays")
    Map<Integer, String> vendorMap = new HashMap<>();
    @SuppressLint("UseSparseArrays")
    Map<Integer, String> subtypeMap = new HashMap<>();
    String subtype_index, vendor_index;
    private EditText mOtpView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mNameView = findViewById(R.id.name);
        mEmailView = findViewById(R.id.email);

        mMobileView = findViewById(R.id.mobile);
        mZipCodeView = findViewById(R.id.zipCode);
        mPasswordView = findViewById(R.id.password);

        mAreYouVendor = findViewById(R.id.areYouVendor);
        mVendorTitle = findViewById(R.id.vendor_title);
        mVendorTitle.setVisibility(View.GONE);
        mVendorRegistrationNo = findViewById(R.id.vendor_registration_no);
        mVendorRegistrationNo.setVisibility(View.GONE);
        mVendorDescription = findViewById(R.id.vendor_description);
        mVendorDescription.setVisibility(View.GONE);
        mVendorId = findViewById(R.id.vendor_id);
        mVendorId.setVisibility(View.GONE);
        mSubtypeId = findViewById(R.id.subtype_id);
        mSubtypeId.setVisibility(View.GONE);

        mAreYouVendor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mVendorTitle.setVisibility(View.VISIBLE);
                mVendorRegistrationNo.setVisibility(View.VISIBLE);
                mVendorDescription.setVisibility(View.VISIBLE);
                mVendorId.setVisibility(View.VISIBLE);
                mSubtypeId.setVisibility(View.VISIBLE);

                if (vendorMap.isEmpty()) {
                    getVendorSpinnerData();
                    getSubtypeSpinnerData();
                }
            }
        });

        mOtpView = findViewById(R.id.otp);

        final Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();

            }
        });

        try {
            if (SharedPref.getInstance(getApplicationContext()).getString(Constants.SharePrefName.USER_DETAILS) != null) {
                JSONObject jsonObj = new JSONObject(SharedPref.getInstance(getApplicationContext()).getString(Constants.SharePrefName.USER_DETAILS)).getJSONObject("status");
                individual_id = jsonObj.optString("individual_id");
                setTitle("Update Profile");
                String name = jsonObj.optString("name");
                name = name.replaceAll("_", " ");
                mNameView.setText(name);
                mMobileView.setText(jsonObj.optString("mobile"));
                mMobileView.setFreezesText(true);
                mPasswordView.setText(jsonObj.optString("password"));
                mEmailView.setText(jsonObj.optString("email"));
                mEmailView.setFreezesText(true);
                mZipCodeView.setText(jsonObj.optString("zipCode"));
                mZipCodeView.setFreezesText(true);

                String isUserVendor = jsonObj.optString("isVendor");
                if (isUserVendor != null && !isUserVendor.isEmpty()) {
                    mAreYouVendor.setChecked(true);
                    if (vendorMap.isEmpty()) {
                        getVendorSpinnerData();
                        getSubtypeSpinnerData();
                    }
                    mVendorTitle.setVisibility(View.VISIBLE);
                    mVendorTitle.setText(jsonObj.optString("vendorTitle"));
                    mVendorRegistrationNo.setVisibility(View.VISIBLE);
                    mVendorRegistrationNo.setText(jsonObj.optString("vendorRegistrationNo"));
                    mVendorDescription.setVisibility(View.VISIBLE);
                    mVendorDescription.setText(jsonObj.optString("vendorDescription"));
                    mVendorId.setVisibility(View.VISIBLE);
                    mVendorId.setSelection(Integer.parseInt(jsonObj.optString("vendorCategoryId")));
                    mSubtypeId.setVisibility(View.VISIBLE);
                    mSubtypeId.setSelection(Integer.parseInt(jsonObj.optString("vendorSubtypeId")));
                }

                mEmailSignInButton.setText("Update");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mNameView.setError(null);
        mEmailView.setError(null);
        mMobileView.setError(null);
        mZipCodeView.setError(null);
        mPasswordView.setError(null);
        mOtpView.setError(null);
        mVendorTitle.setError(null);
        mVendorRegistrationNo.setError(null);
        mVendorDescription.setError(null);

        // Store values at the time of the login attempt.
        String name = mNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String mobile = mMobileView.getText().toString();
        String zipCode = mZipCodeView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean isVendor = mAreYouVendor.isChecked();
        String vendorTitle = mVendorTitle.getText().toString();
        String vendorRegistrationNo = mVendorRegistrationNo.getText().toString();
        String vendorDescription = mVendorDescription.getText().toString();
        int selectedVendorCategoryIndex = mVendorId.getSelectedItemPosition();
        int selectedVendorSubtypeIndex = mSubtypeId.getSelectedItemPosition();
        String otpByUser = mOtpView.getText().toString();

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
        } else if (generatedOtp > 0 && TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        } else if (generatedOtp > 0 && TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!TextUtils.isEmpty(email) && !isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else if (generatedOtp > 0 && TextUtils.isEmpty(zipCode)) {
            mZipCodeView.setError(getString(R.string.error_field_required));
            focusView = mZipCodeView;
            cancel = true;
        } else if (generatedOtp > 0 && isVendor && TextUtils.isEmpty(vendorTitle)) {
            mVendorTitle.setError(getString(R.string.error_field_required));
            focusView = mVendorTitle;
            cancel = true;
        } else if (generatedOtp > 0 && isVendor && TextUtils.isEmpty(vendorRegistrationNo)) {
            mVendorRegistrationNo.setError(getString(R.string.error_field_required));
            focusView = mVendorRegistrationNo;
            cancel = true;
        } else if (generatedOtp > 0 && isVendor && TextUtils.isEmpty(vendorDescription)) {
            mVendorDescription.setError(getString(R.string.error_field_required));
            focusView = mVendorDescription;
            cancel = true;
        } else if (generatedOtp > 0 && isVendor && (selectedVendorCategoryIndex == 0)) {
            focusView = mVendorId;
            cancel = true;
        } else if (generatedOtp > 0 && isVendor && (selectedVendorSubtypeIndex == 0)) {
            focusView = mSubtypeId;
            cancel = true;
        } else if (generatedOtp > 0 && Integer.parseInt(otpByUser) != generatedOtp) {
            mOtpView.setError("Invalid OTP");
            focusView = mOtpView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showDialog();
            String tag_json_obj = "json_user_authentication_req";
            HashMap<String, String> params = new HashMap<>();
            params.put("individual_id", individual_id);
            params.put("name", name);
            params.put("email", email);
            params.put("mobile", mobile);
            params.put("zipCode", zipCode);
            params.put("password", password);

            if (isVendor) {
                params.put("vendorTitle", vendorTitle);
                params.put("vendorRegistrationNo", vendorRegistrationNo);
                params.put("vendorDescription", vendorDescription);

                String vendorText = mVendorId.getSelectedItem().toString();
                if (vendorText.equalsIgnoreCase(vendorMap.get(selectedVendorCategoryIndex))) {
                    params.put("vendorCategoryId", String.valueOf(selectedVendorCategoryIndex));
                    String vendorSubtypeText = String.valueOf(mSubtypeId.getSelectedItem());
                    boolean isVendorSubTypeSelected = false;
                    for (Map.Entry<Integer, String> entry : subtypeMap.entrySet()) {
                        if (entry.getValue().equalsIgnoreCase(vendorSubtypeText + "<+>" + selectedVendorCategoryIndex)) {
                            params.put("vendorSubtypeId", entry.getKey().toString());
                            isVendorSubTypeSelected = true;
                            break;
                        }
                    }
                    if (!isVendorSubTypeSelected) {
                        mAreYouVendor.setError("Please select vendor subtype.");
                        focusView = mAreYouVendor;
                        focusView.requestFocus();
                    }
                } else {
                    mAreYouVendor.setError("Please select vendor category.");
                    focusView = mAreYouVendor;
                    focusView.requestFocus();
                }
            } else {
                params.put("vendorTitle", "");
                params.put("vendorRegistrationNo", "");
                params.put("vendorDescription", "");
                params.put("vendorCategoryId", "");
                params.put("vendorSubtypeId", "");
                params.put("imageName", "");
                params.put("imageUrl", "");
                params.put("imageBitmapString", "");
            }

            params.put("mobileVerificationCode", otpByUser);
            params.put("ipAddress", Utility.getDeviceImei(this));


            new NetworkRequester.RequestBuilder(this)
                    .setRequestType(NetworkConstant.ReqType.POST)
                    .setUrl(NetworkConstant.loginURL)
                    .setListener(this)
                    .setReqParams(new JSONObject(params))
                    .setRequestTag(NetworkConstant.RequestTagType.login)
                    .build()
                    .sendRequest();

        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4 && password.length() <= 10;
    }

    private boolean isMobileValid(String mobile) {
        return mobile.length() <= 10;
    }

    private void getVendorSpinnerData() {
        // Tag used to cancel the request
        String tag_json_obj = "json_vendor_spinner_data_req";
        String selectVendorURL = NetworkConstant.BASE_URL + "/vendor/samcrmSelectVendor";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                selectVendorURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            List<String> vendorList = new ArrayList<>();
                            JSONObject mapObject = response.getJSONObject("status");
                            vendorMap = commonUtility.convertToMap(mapObject);
                            vendorList.add("Select Vendor Category");
                            if (!vendorMap.isEmpty()) {
                                for (int categoryIndex = 1; categoryIndex <= vendorMap.size(); categoryIndex++) {
                                    vendorList.add(vendorMap.get(categoryIndex));
                                }
                            }
                            mVendorId.setAdapter(new ArrayAdapter<>(RegisterActivity.this,
                                    android.R.layout.simple_spinner_item, vendorList));
                            int vendorIndex = vendor_index == null ? 0 : Integer.parseInt(vendor_index) - 1;
                            mVendorId.setSelection(vendorIndex);
                            mVendorId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    reloadSubType(i);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } catch (final JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void getSubtypeSpinnerData() {

        // Tag used to cancel the request
        String tag_json_obj = "json_subtype_spinner_data_req";

        String selectSubtypeURL = NetworkConstant.BASE_URL + "/vendor/samcrmSelectSubtype";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                selectSubtypeURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            JSONObject mapObject = response.getJSONObject("status");
                            subtypeMap = commonUtility.convertToMap(mapObject);
                        } catch (final JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void reloadSubType(int selectedCategoryId) {

        List<String> subtypeList = new ArrayList<>();
        subtypeList.add("Select Vendor Subtype");
        if (selectedCategoryId > 0 && !subtypeMap.isEmpty()) {
            for (Map.Entry<Integer, String> entry : subtypeMap.entrySet()) {
                StringTokenizer subtypeCategory = new StringTokenizer(entry.getValue(), "<+>");
                String label = subtypeCategory.nextToken();
                int categoryId = Integer.parseInt(subtypeCategory.nextToken());
                if (categoryId == selectedCategoryId) {
                    subtypeList.add(label);
                }
            }
        }
        mSubtypeId.setAdapter(new ArrayAdapter<>(RegisterActivity.this,
                android.R.layout.simple_spinner_item, subtypeList));
        int subtypeIndex = subtype_index == null ? 0 : Integer.parseInt(subtype_index) - 1;
        mSubtypeId.setSelection(subtypeIndex);
        mSubtypeId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_register;
    }

    @Override
    public void onResponse(Object data, int requestTag) {
        Log.d(TAG, data.toString());
        try {
            hideDialog();
            JSONObject jsonObj = new JSONObject(data.toString());
            // When the JSON response has status boolean value as samcrmAuthentication signed with true
            if (jsonObj.getBoolean("status")) {
                generatedOtp = 0;
                Toast.makeText(getApplicationContext(), "User registrantion / authentication / updation successful!", Toast.LENGTH_LONG).show();
                JSONArray vendorsList = jsonObj.getJSONArray("tag");
                JSONObject object = new JSONObject();
                object.put("status", new JSONObject(jsonObj.getString("errorMessage")));

                Intent isService = new Intent(getApplicationContext(), RegisterActivity.class);
                stopService(isService);
                SharedPref.getInstance(RegisterActivity.this).saveString(Constants.SharePrefName.USER_DETAILS, object.toString());
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
                    mOtpView.setError("Enter OTP which is sent to your mobile number. \nAnd click the register button again!");
                    mOtpView.requestFocus();
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

