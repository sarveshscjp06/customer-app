package com.samcrm.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.samcrm.R;
import com.samcrm.Constants;
import com.samcrm.activity_manager.BaseActivity;
import com.samcrm.adpater.VendorListAdapter;
import com.samcrm.bannerslider.Slider;
import com.samcrm.bannerslider.service.PicassoImageLoadingService;
import com.samcrm.network.AppController;
import com.samcrm.vo.VendorVO;
import com.samcrm.CircleImageView;

import com.samcrm.adpater.BannerSliderAdapter;

import com.samcrm.network.CloudStorage;
import com.samcrm.network.NetworkConstant;
import com.samcrm.network.NetworkRequester;
import com.samcrm.utility.SharedPref;
import com.samcrm.utility.Utility;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, VendorListAdapter.OnItemClickListener {
    private static String TAG = MainActivity.class.getSimpleName();
    private Slider slider;
    private List<VendorVO> vendorVOList;
    private RecyclerView recyclerView;
    JSONObject individualDataMap;
    TextView upload_profile_pic, individual_name, individual_email;
    CircleImageView individual_ImageView;
    FloatingActionButton floatingActionButton;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;

    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestStoragePermission();
        if (SharedPref.getInstance(getApplicationContext()).getString(Constants.SharePrefName.USER_DETAILS) != null) {
            try {
                individualDataMap = new JSONObject(SharedPref.getInstance(getApplicationContext()).getString(Constants.SharePrefName.USER_DETAILS));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        upload_profile_pic = header.findViewById(R.id.uploadProfilePic);
        individual_ImageView = header.findViewById(R.id.individualImageView);
        individual_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showFileChooser();
                    upload_profile_pic.setVisibility(view.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //Initializing views
        upload_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadImage();
                upload_profile_pic.setVisibility(view.INVISIBLE);
            }
        });
        getProductApi();
        individual_name = header.findViewById(R.id.individualName);
        individual_email = header.findViewById(R.id.individualEmail);


        if (individualDataMap == null) {
            Toast.makeText(getApplicationContext(), "Kindly login to the SAM-CRM App.", Toast.LENGTH_LONG).show();
        }
        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setupSliderView();
        setProfile();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                //  return true;
                break;
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
        toolbar.inflateMenu(R.menu.menu_home);
        MenuItem cartMenuItem = toolbar.getMenu().findItem(R.id.cart);
        MenuItemCompat.setActionView(cartMenuItem, R.layout.menu_cart_badge);
        Utility.updateCartCounterUI(toolbar, this);
        final FrameLayout cartActionView = (FrameLayout) MenuItemCompat.getActionView(cartMenuItem);
        ImageView container = cartActionView.findViewById(R.id.cart_btn);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.onCartMenuItemClicked(MainActivity.this);
            }
        });
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                logoutSamcrmUser();
                break;
            case R.id.orders:
                Intent newIntent = new Intent(getApplicationContext(), OrderActivity.class);
                startActivity(newIntent);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupSliderView() {
        slider = findViewById(R.id.banner_slider);
        Slider.init(new PicassoImageLoadingService(this));
        slider.postDelayed(new Runnable() {
            @Override
            public void run() {
                slider.setAdapter(new BannerSliderAdapter());
                slider.setSelectedSlide(0);
            }
        }, 1500);
        slider.setAnimateIndicators(true);
        slider.setInterval(2500);

    }


    private void logoutSamcrmUser() {

//        showDialog();
        // Tag used to cancel the request
        String tag_json_obj = "json_logout_req";
        HashMap<String, String> params = new HashMap<>();
        try {
            params.put("mobile", individualDataMap.getJSONObject("status").optString("mobile"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("ipAddress", Utility.getDeviceImei(this));
        String userDataURL = NetworkConstant.BASE_URL + "/login/logoutSamcrmUser";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                userDataURL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        hideDialog();
                        Log.d(TAG, response.toString());
                        try {
                            if (response.getBoolean("status")) {

                                individualDataMap.remove("status");
                                SharedPref.getInstance(getApplicationContext()).clear(Constants.SharePrefName.USER_DETAILS);
                                Toast.makeText(getApplicationContext(), "User logged out successfully.", Toast.LENGTH_LONG).show();
                                stopService(new Intent(getApplicationContext(), MainActivity.class));
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else if (!response.getString("errorMessage").isEmpty()) {
                                Toast.makeText(getApplicationContext(), response.getString("errorMessage"), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Something Wrong. Please try again!", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
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


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Nothing to worry. The image choose, will remain your property.", Toast.LENGTH_LONG).show();
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE || requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {

            //If permission is granted
            for (int grantIndex = 0; grantIndex < grantResults.length; grantIndex++) {
                if (grantResults[grantIndex] == PackageManager.PERMISSION_GRANTED) {
                    //Displaying a toast
                    Toast.makeText(this, "Permission granted now you can read the " + permissions[grantIndex], Toast.LENGTH_LONG).show();
                } else {
                    //Displaying another toast if permission is not granted
                    Toast.makeText(this, "Oops you just denied the permission for" + permissions[grantIndex], Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            String path = filePath.getPath();
            String finalPath = "/" + path.substring(path.indexOf('s'));
            File file = new File(finalPath);
            if (file.length() > 16384) {
                Toast.makeText(getApplicationContext(), "Image size should not be more than 16kb.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    individual_ImageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void uploadImage() {
        try {
            String bucketName = "raagatechmusic.appspot.com";
            //getting the actual path of the image
            String path = filePath.getPath();
            String finalPath = "/" + path.substring(path.indexOf('s'));
            String imageName = "IMG" + individualDataMap.getJSONObject("status").optString("mobile") + "_" +
                    individualDataMap.getJSONObject("status").optString("individual_id");
            CloudStorage.uploadFile(bucketName, finalPath, imageName);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.updateCartCounterUI(toolbar, this);
        getProductApi();

    }

    private void getProductApi() {

        JSONObject js = new JSONObject();
        try {
            js.put("vendorId", Utility.getVendorId(MainActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new NetworkRequester.RequestBuilder(this)
                .setRequestType(NetworkConstant.ReqType.GET)
                .setUrl(NetworkConstant.samcrmVendorsList + "?mobile=&vendorCategoryId=&vendorSubtypeId=&postalCode=" + Utility.getZipCode(MainActivity.this))
                .setListener(this)
                .setRequestTag(NetworkConstant.RequestTagType.VENDOR_LIST)
                .build()
                .sendRequest();


    }

    @Override
    public void onResponse(Object data, int requestTag) {
        Log.e("response", data.toString());
        switch (requestTag) {
            case NetworkConstant.RequestTagType.VENDOR_LIST:
                try {
                    vendorVOList = new ArrayList<>();
                    vendorVOList.clear();
                    JSONObject jsonObj = new JSONObject(data.toString());
                    JSONArray array = jsonObj.getJSONArray("tag");
                    for (int i = 0; i < array.length(); i++) {
                        vendorVOList.add(new VendorVO((JSONObject) array.get(i)));

                    }
                    VendorListAdapter adapter = new VendorListAdapter(MainActivity.this, vendorVOList, this);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }


    }

    @Override
    public void onError(VolleyError error, int requestTag) {
        Log.e("response", error.toString());
    }


    @Override
    public void onGetItemClick(VendorVO vendorVO) {
        Intent newIntent = new Intent(getApplicationContext(), VendorItemListActivity.class);
        newIntent.putExtra("vendorVO", vendorVO);
        startActivity(newIntent);
    }

    private void setProfile() {
        if (individualDataMap != null) {
            try {
                String profileName = individualDataMap.getJSONObject("status").optString("name");
                profileName = profileName.replaceAll("_", " ");
                individual_name.setText(profileName);
                individual_email.setText(individualDataMap.getJSONObject("status").optString("email"));

                String imageName = "IMG" + individualDataMap.getJSONObject("status").optString("mobile") + "_" +
                        individualDataMap.getJSONObject("status").optString("individual_id");//"IMG_20180428_173313.jpg"
                Bitmap image = CloudStorage.downloadFile("raagatechmusic.appspot.com", imageName, null);
                if (image != null) {
                    individual_ImageView.setImageBitmap(image);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
