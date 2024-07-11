package com.example.pppb51_tubes02_b_f;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

public class ControlPresenter {
    private static final String TAG = "ControlPresenter";
    private IControlActivity ui;
    private IMultipleRequest connectFragment;

    public ControlPresenter(IControlActivity ui, IMultipleRequest connectFragment) {
        this.ui = ui;
        this.connectFragment = connectFragment;
    }

    public void getRoutes(Context context) {
        // Use the modified GetRoutesAPI with mock data
        GetRoutesAPI api = new GetRoutesAPI(connectFragment, context);
        api.execute();
    }

    public void getOrders(Context context) {
        // Mock data for orders
        String mockOrdersJson = "[{\"orderId\":\"order_1\",\"details\":\"Order details 1\"},{\"orderId\":\"order_2\",\"details\":\"Order details 2\"}]";
        try {
            JSONArray ordersArray = new JSONArray(mockOrdersJson);
            connectFragment.onResponse(ordersArray.toString(), "getorders");
        } catch (JSONException e) {
            Log.e(TAG, "JSONException in getOrders: " + e.getMessage());
            connectFragment.onError(e.getMessage());
        }
    }
}
