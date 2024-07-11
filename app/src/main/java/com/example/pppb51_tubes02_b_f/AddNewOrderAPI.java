package com.example.pppb51_tubes02_b_f;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class AddNewOrderAPI {
    private IMultipleRequest connectFragment;
    private Context context;
    private Gson gson;

    public AddNewOrderAPI(IMultipleRequest connectFragment, Context context){
        this.connectFragment = connectFragment;
        this.context = context;
    }

    public void execute(OrderInput input) throws JSONException {
        gson = new Gson();
        String strInput = gson.toJson(input);
        JSONObject json = new JSONObject(strInput);
        // Mock data for order response with message
        String mockResponse = "{ \"order_id\": \"mock_order_123\", \"status\": \"success\", \"message\": \"Order placed successfully\" }";
        processResult(mockResponse);
    }

    private void processResult(String response){
        try {
            connectFragment.onResponse(response, "order");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processError(String error){
        connectFragment.onError(error);
    }
}
