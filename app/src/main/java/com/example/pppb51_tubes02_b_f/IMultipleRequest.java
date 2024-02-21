package com.example.pppb51_tubes02_b_f;

import org.json.JSONException;

public interface IMultipleRequest {
    public void onResponse(Object response, String type) throws JSONException;
    public void onError(String error);
}
