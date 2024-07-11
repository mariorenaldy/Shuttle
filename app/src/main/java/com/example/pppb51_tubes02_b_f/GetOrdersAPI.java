package com.example.pppb51_tubes02_b_f;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetOrdersAPI {
    private IMultipleRequest connectFragment;
    private Context context;

    public GetOrdersAPI(IMultipleRequest connectFragment, Context context){
        this.connectFragment = connectFragment;
        this.context = context;
    }

    public void execute() throws JSONException {
        // Mock data for orders
        String mockOrdersJson = "[{\"order_id\":\"order_1\",\"details\":\"Order details 1\"},{\"order_id\":\"order_2\",\"details\":\"Order details 2\"}]";
        try {
            JSONArray ordersArray = new JSONArray(mockOrdersJson);
            processResult(ordersArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            processError(e.getMessage());
        }
    }

    private void processResult(String arrayVal){
        try {
            connectFragment.onResponse(arrayVal, "getorders");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processError(String error){
        connectFragment.onError(error);
    }
}
