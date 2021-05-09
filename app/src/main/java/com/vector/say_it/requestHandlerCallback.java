package com.vector.say_it;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public interface requestHandlerCallback {
    void callback(JSONObject response);
    void callback(JSONArray response);
    void callbackError(VolleyError e);
}
