package com.vector.say_it;

import org.json.JSONArray;
import org.json.JSONObject;

public interface requestHandlerCallback {
    void callback(JSONObject response);
    void callback(JSONArray response);
}
