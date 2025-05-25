package com.samcrm.network;

import com.android.volley.VolleyError;


public interface NetworkResponseListener<T>  {
    void onResponse(T data , int requestTag);
    void onError(VolleyError error , int requestTag);

}