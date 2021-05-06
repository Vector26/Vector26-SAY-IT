package com.vector.say_it;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestHandler implements requestHandlerCallback{
    String url;
    int Method;
    JSONArray data;
    Context context;
    SharedPreferences sharedPreferences;

    @Override
    public void callback(JSONArray response) {
        Log.i("response",response.toString());
    }

    public RequestHandler(Boolean many,String url, int method, @Nullable JSONArray data, Context context, SharedPreferences shared) {
        this.url = url;
        Method = method;
        this.data = data;
        this.context = context;
        this.sharedPreferences=shared;
        RequestQueue queue = Volley.newRequestQueue(this.context);
        if(!many){
        try{
            JsonObjectRequest req = new JsonObjectRequest(this.Method,this.url, (JSONObject) this.data.get(0),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                                callback(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.getStackTrace();
                }
            }){
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    HashMap<String, String> headers = new HashMap<String, String>();
                    if(sharedPreferences.getString("Auth-Token","").length()>0) {
                        headers.put("Authorization", "Token " + sharedPreferences.getString("Auth-Token", ""));
                    }
                    Log.i("headers",headers.toString());
                    return headers;
                }
            };
            queue.add(req);
        } catch(Exception e) {
            e.printStackTrace();
        }
        }
        else if(many==true){
            try{
                JsonArrayRequest req = new JsonArrayRequest(this.Method,this.url, this.data,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                callback(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.getStackTrace();
                    }
                }){
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {

                        HashMap<String, String> headers = new HashMap<String, String>();
                        if(sharedPreferences.getString("Auth-Token","").length()>0) {
                            headers.put("Authorization", "Token " + sharedPreferences.getString("Auth-Token", ""));
                        }
                        Log.i("headers",headers.toString());
                        return headers;
                    }
                };
                queue.add(req);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void callback(JSONObject response) {
        Log.i("Response",response.toString());
    }
}
