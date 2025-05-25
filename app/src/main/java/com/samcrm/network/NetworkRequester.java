package com.samcrm.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.BuildConfig;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.samcrm.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class NetworkRequester {
    public static NetworkRequester networkRequester;
    private NetworkResponseListener listener;
    private RequestQueue requestQueue;
    private JSONObject reqParams;
    private int requestType;

    private Context context;
    private int requestTag;
    private String url;


    private NetworkRequester(RequestBuilder builder) {
        getInstance(builder);
    }

    public NetworkRequester(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    private NetworkRequester getInstance(RequestBuilder builder) {
        if (networkRequester == null) {
            networkRequester = new NetworkRequester(builder.context);
        }
        networkRequester.requestType = builder.requestType;
        networkRequester.requestTag = builder.requestTag;
        networkRequester.listener = builder.listener;
        networkRequester.reqParams = builder.reqParams;
        networkRequester.url = builder.url;
        return networkRequester;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        return requestQueue;
    }


    public void sendRequest() {
        int reqType = networkRequester.requestType == NetworkConstant.ReqType.GET ? Request.Method.GET : Request.Method.POST;
        Request request = null;
        request = new JsonObjectRequest(reqType, networkRequester.url, networkRequester.reqParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                networkRequester.listener.onResponse(response, networkRequester.requestTag);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                networkRequester.listener.onError(error, networkRequester.requestTag);


            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Set<Map.Entry<String, String>> entrySet = params.entrySet();
                Iterator<Map.Entry<String, String>> itr = entrySet.iterator();
                while (itr.hasNext()) {
                    Map.Entry<String, String> entry = itr.next();
                    params.put(entry.getKey(), entry.getValue());
                }
                if (networkRequester.reqParams != null) {
                    JSONObject authJson = networkRequester.reqParams.optJSONObject("auth");
                    if (authJson != null) {
                        params.put("Authorization", authJson.optString("Authorization"));
                    }
                }
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                JSONArray paramArr = networkRequester.reqParams.optJSONArray("params");
                if (paramArr != null) {
                    for (int i = 0; i < paramArr.length(); i++) {
                        try {
                            JSONObject paramJson = paramArr.getJSONObject(i);
                            Iterator<String> reqParamsKeysItr = paramJson.keys();
                            while (reqParamsKeysItr.hasNext()) {
                                String key = reqParamsKeysItr.next();
                                params.put(key, networkRequester.reqParams.optString(key));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return params;
            }

            @Override
            public byte[] getBody() {
                JSONObject bodyJson = new JSONObject();
                JSONArray paramArr = networkRequester.reqParams.optJSONArray("params");
                if (paramArr != null) {
                    for (int i = 0; i < paramArr.length(); i++) {
                        try {
                            JSONObject paramJson = paramArr.getJSONObject(i);
                            Iterator<String> reqParamsKeysItr = paramJson.keys();
                            while (reqParamsKeysItr.hasNext()) {
                                String key = reqParamsKeysItr.next();
                                bodyJson.put(key, paramJson.opt(key));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                return bodyJson.toString().getBytes();
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                if (networkRequester.reqParams != null) {
                    String charset = Utility.fetchValueFromReqParam("charset", networkRequester.reqParams);
                    if (charset != null) {
                        try {
                            String utf8String = new String(response.data, charset);
                            return Response.success(new JSONObject(utf8String), HttpHeaderParser.parseCacheHeaders(response));
                        } catch (UnsupportedEncodingException e) {
                            return Response.error(new ParseError(e));
                        } catch (JSONException e) {
                            return Response.error(new ParseError(e));
                        }
                    } else {
                        return super.parseNetworkResponse(response);
                    }

                } else {
                    return super.parseNetworkResponse(response);
                }
            }
        };


        if (request != null) {
            request.setRetryPolicy(new DefaultRetryPolicy(NetworkConstant.Timeout.REQUEST_TIME, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setTag(BuildConfig.LIBRARY_PACKAGE_NAME);
            request.setShouldCache(false);
            networkRequester.requestQueue.add(request);
        }

    }


    public static class RequestBuilder {

        private Context context;
        private String url;
        private int requestType;

        private JSONObject reqParams;
        private int requestTag;
        private NetworkResponseListener listener;

        public RequestBuilder(Context ctx) {
            this.context = ctx;
        }

        public RequestBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public RequestBuilder setRequestType(int requestType) {
            this.requestType = requestType;
            return this;
        }


        public RequestBuilder setReqParams(JSONObject reqParams) {
            this.reqParams = reqParams;
            return this;
        }

        public RequestBuilder setRequestTag(int requestTag) {
            this.requestTag = requestTag;
            return this;
        }


        public RequestBuilder setListener(NetworkResponseListener listener) {
            this.listener = listener;
            return this;
        }

        public NetworkRequester build() {
            return new NetworkRequester(this);
        }
    }


}

