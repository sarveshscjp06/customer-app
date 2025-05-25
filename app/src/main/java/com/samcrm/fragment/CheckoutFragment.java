package com.samcrm.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.samcrm.Constants;
import com.samcrm.R;
import com.samcrm.activity_manager.BaseActivity;
import com.samcrm.activity_manager.BaseFragment;
import com.samcrm.adpater.AddressListAdapter;
import com.samcrm.network.NetworkConstant;
import com.samcrm.network.NetworkRequester;
import com.samcrm.utility.Utility;
import com.samcrm.vo.CartVO;
import com.samcrm.vo.ItemCartVO;
import com.samcrm.vo.ShippingAddressVO;
import com.samcrm.vo.VendorCatItemVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CheckoutFragment extends BaseFragment implements AddressListAdapter.OnItemClickListener {
    private TextView name, addressOne, addressTwo, addressThree, totalPrice, total_payment;
    private TextView mobile, applyCoupon, coupon, dis, couponMsg;
    private CartVO cartVO;
    private ShippingAddressVO addAddressVo;
    private TextView changeAddress;
    private Button submit;
    private Dialog dialog;
    private TableRow applyCouponLayout, couponLayout, DisLayout, couponMsgLayout;
    int totalAmount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_checkout, container, false);
        bindView(rootView);
        return rootView;
    }

    private void bindView(View view) {
        dialog = new Dialog(context);
        name = view.findViewById(R.id.name);
        submit = view.findViewById(R.id.submit);
        totalPrice = view.findViewById(R.id.totalPrice);
        addressOne = view.findViewById(R.id.address_one);
        addressTwo = view.findViewById(R.id.address_two);
        addressThree = view.findViewById(R.id.address_three);
        changeAddress = view.findViewById(R.id.changeAddress);
        mobile = view.findViewById(R.id.mobile);
        applyCouponLayout = view.findViewById(R.id.applyCouponLayout);
        couponLayout = view.findViewById(R.id.couponLayout);
        DisLayout = view.findViewById(R.id.DisLayout);
        couponMsgLayout = view.findViewById(R.id.couponMsgLayout);
        total_payment = view.findViewById(R.id.total_payment);
        applyCoupon = view.findViewById(R.id.applyCoupon);
        coupon = view.findViewById(R.id.coupon);
        dis = view.findViewById(R.id.dis);
        couponMsg = view.findViewById(R.id.couponMsg);
        addAddressVo = new ShippingAddressVO();
        if (getArguments() != null) {
            cartVO = (CartVO) getArguments().getSerializable("cartVO");
            name.setText(cartVO.getDelivery_person());
            mobile.setText("" + cartVO.getMobile());
            addressOne.setText(cartVO.getStreet());
            addressTwo.setText(cartVO.getCountry());
            addressThree.setText(cartVO.getZipcode());
            totalPrice.setText("₹ " + new ItemCartVO().totalPrice(cartVO.getItemCartVOS()));
            setTotalPriceAfterDis(0);
        }

        changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllAddressRequest();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartVO != null)
                    requestForOrderCompete();
            }
        });

        applyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartVO != null)
                    requestForCoupon();
            }
        });

    }

    private void requestForCoupon() {
        progressDialog.show();
        JSONObject reqParams = Utility.getRequestParam(context, null);
        new NetworkRequester.RequestBuilder(context)
                .setRequestType(NetworkConstant.ReqType.GET)
                .setUrl(
                        NetworkConstant.samcrmDiscountCoupon
                                + "?vendor_id=" + Utility.getVendorId(context)
                                + "&customer_id=" + "9" //  Utility.getCus(context))
                                + "&order_id=" + cartVO.getOrder_id()
                                + "&total_price=" + new ItemCartVO().totalPrice(cartVO.getItemCartVOS()))
                .setListener(this)
                .setReqParams(reqParams)
                .setRequestTag(NetworkConstant.RequestTagType.samcrmDiscountCoupon)
                .build()
                .sendRequest();
    }

    private void requestForOrderCompete() {
        progressDialog.show();
        JSONObject reqParams = Utility.getRequestParam(context, null);
        new NetworkRequester.RequestBuilder(context)
                .setRequestType(NetworkConstant.ReqType.GET)
                .setUrl(
                        NetworkConstant.samcrmConfirmOrder
                                + "?vendor_id=" + Utility.getVendorId(context)
                                + "&addressId=" + "1" // Address id
                                + "&customer_id=" + "9" //  Utility.getCus(context))
                                + "&order_id=" + cartVO.getOrder_id()
                                + "&total_price" + totalAmount
                                + "&couponCode" + coupon.getText().toString())


                .setListener(this)
                .setReqParams(reqParams)
                .setRequestTag(NetworkConstant.RequestTagType.samcrmConfirmOrder)
                .build()
                .sendRequest();
    }

    @Override
    public void onResponse(Object data, int requestTag) {
        progressDialog.dismiss();
        switch (requestTag) {

            case NetworkConstant.RequestTagType.samcrmListContacts:
                try {
                    ArrayList<ShippingAddressVO> itemVOS = new ArrayList<>();
                    itemVOS.clear();
                    JSONObject jsonObj = new JSONObject(data.toString());
                    JSONArray array = jsonObj.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        itemVOS.add(new ShippingAddressVO((JSONObject) array.get(i)));
                    }
                    showDialog(itemVOS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case NetworkConstant.RequestTagType.samcrmAddContact:
                setUiForAddress(addAddressVo);
                break;
            case NetworkConstant.RequestTagType.samcrmDiscountCoupon:

                Log.e("response", data.toString());
                try {
                    JSONObject jsonObj = new JSONObject(data.toString());
                  //  if (jsonObj.getInt("tag") == 0) {
                   //     couponMsg.setText("No coupon ");
                   //     dis.setText("0");
                    //    coupon.setText("");
                    //    couponMsgLayout.setVisibility(View.VISIBLE);
                     //   DisLayout.setVisibility(View.GONE);
                     //   applyCouponLayout.setVisibility(View.VISIBLE);

                  //  } else {
                        couponMsgLayout.setVisibility(View.VISIBLE);
                        DisLayout.setVisibility(View.VISIBLE);
                        couponMsgLayout.setVisibility(View.VISIBLE);
                        applyCouponLayout.setVisibility(View.GONE);
                        couponMsg.setText("coupon apply");
                        dis.setText(jsonObj.getString("tag"));
                        coupon.setText(jsonObj.getString("errorMessage"));
                        setTotalPriceAfterDis(jsonObj.getInt("tag"));


                   // }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;
            case NetworkConstant.RequestTagType.samcrmConfirmOrder:

                Log.e("response", data.toString());
                try {
                    JSONObject jsonObj = new JSONObject(data.toString());
                    if (jsonObj.getBoolean("status")) {
                        Utility.t(context, "Success");
                        ((BaseActivity) context).loadFragment(Constants.Fragment.ORDER_SUCCESS, null, Constants.FragTransaction.ADD);
                    } else {

                        Utility.t(context, "order Fail");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;


        }


    }

    private void setTotalPriceAfterDis(int amount) {
        total_payment.setText("₹ " + (new ItemCartVO().totalPrice(cartVO.getItemCartVOS()) - amount));
        this.totalAmount = (new ItemCartVO().totalPrice(cartVO.getItemCartVOS()) - amount);
    }

    @Override
    public void onError(VolleyError error, int requestTag) {
        progressDialog.dismiss();
        Log.e("error", error.toString());
    }


    @Override
    public String getToolbarTitle() {
        return getString(R.string.app_name);
    }

    private void showDialog(ArrayList<ShippingAddressVO> itemVOS) {


        dialog.setContentView(R.layout.custom_address_dialog);
        dialog.setTitle("Address");

        final TextView noData = dialog.findViewById(R.id.no_data);
        final RecyclerView recyclerView = dialog.findViewById(R.id.recycler_view);
        final RelativeLayout addressFieldLayout = dialog.findViewById(R.id.addressFieldLayout);
        final RelativeLayout addressListLayout = dialog.findViewById(R.id.addressListLayout);
        final FloatingActionButton addnewaddress = dialog.findViewById(R.id.addnewaddress);

        final EditText inputName = dialog.findViewById(R.id.name);
        final EditText inputMobile = dialog.findViewById(R.id.mobile);
        final EditText inputAddress = dialog.findViewById(R.id.address);
        final EditText inputZip = dialog.findViewById(R.id.zip);
        final EditText inputComment = dialog.findViewById(R.id.comment);
        final EditText email = dialog.findViewById(R.id.email);
        final Button save = (Button) dialog.findViewById(R.id.register);
        final Button oldAddress = (Button) dialog.findViewById(R.id.old);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        if (itemVOS.size() == 0) {
            noData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        AddressListAdapter adapter = new AddressListAdapter(context, itemVOS, this);
        recyclerView.setAdapter(adapter);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                addAddressVo.setAddress(inputAddress.getText().toString());
                addAddressVo.setMobile(inputMobile.getText().toString());
                addAddressVo.setEmail(email.getText().toString());
                addAddressVo.setName(inputName.getText().toString());
                addAddressVo.setZipcode(inputZip.getText().toString());
                addAddressVo.setComment(inputComment.getText().toString());
                dialog.dismiss();
                requestForAddAddress();

            }
        });


        oldAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressListLayout.setVisibility(View.VISIBLE);
                addressFieldLayout.setVisibility(View.GONE);


            }
        });
        addnewaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressListLayout.setVisibility(View.GONE);
                addressFieldLayout.setVisibility(View.VISIBLE);

            }
        });

        dialog.show();
    }

    private void requestForAddAddress() {
        JSONObject js = new JSONObject();
        try {
            js.put("vendor_group_id", Utility.getVendorId(context));
            js.put("name", addAddressVo.getName());
            js.put("email", addAddressVo.getEmail());
            js.put("mobile", addAddressVo.getMobile());
            js.put("zipCode", addAddressVo.getZipcode());
            js.put("dob", addAddressVo.getDob());
            js.put("address", addAddressVo.getAddress());
            js.put("comment", addAddressVo.getComment());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject reqParams = Utility.getRequestParam(context, null);
        Utility.addParamJson(reqParams, js);
        new NetworkRequester.RequestBuilder(context)
                .setRequestType(NetworkConstant.ReqType.POST)
                .setUrl(NetworkConstant.samcrmAddContact)
                .setListener(this)
                .setReqParams(reqParams)
                .setRequestTag(NetworkConstant.RequestTagType.samcrmAddContact)
                .build()
                .sendRequest();

    }

    @Override
    public void onGetShippingAddress(ShippingAddressVO ShippingAddressVO) {
        dialog.dismiss();
        setUiForAddress(ShippingAddressVO);
    }

    private void setUiForAddress(ShippingAddressVO shippingAddressVO) {
        name.setText(shippingAddressVO.getName());
        mobile.setText("" + shippingAddressVO.getMobile());
        addressOne.setText(shippingAddressVO.getAddress());
        addressTwo.setText(shippingAddressVO.getCountry_code());
        addressThree.setText(shippingAddressVO.getZipcode());
    }

    private void getAllAddressRequest() {
        JSONObject js = new JSONObject();
        try {
            js.put("vendor_group_id", Utility.getVendorId(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject reqParams = Utility.getRequestParam(context, null);
        Utility.addParamJson(reqParams, js);
        new NetworkRequester.RequestBuilder(context)
                .setRequestType(NetworkConstant.ReqType.POST)
                .setUrl(NetworkConstant.samcrmListContacts)
                .setListener(this)
                .setReqParams(reqParams)
                .setRequestTag(NetworkConstant.RequestTagType.samcrmListContacts)
                .build()
                .sendRequest();
    }


}
