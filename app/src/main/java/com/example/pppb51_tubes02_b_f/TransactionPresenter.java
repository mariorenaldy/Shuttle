package com.example.pppb51_tubes02_b_f;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TransactionPresenter {
    private ITransactionFragment ui;
    private IMultipleRequest connectFragment;
    private static List<JSONObject> orders = new ArrayList<>();

    public TransactionPresenter(IMultipleRequest connectFragment, ITransactionFragment ui) {
        this.connectFragment = connectFragment;
        this.ui = ui;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void order(Context context, OrderInput input) throws JSONException {
        // Check if network is available
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if (networkInfo != null) {
            AddNewOrderAPI worker = new AddNewOrderAPI(connectFragment, context);
            worker.execute(input);
            // Save the order locally
            JSONObject order = new JSONObject();
            order.put("source", input.getSource());
            order.put("destination", input.getDestination());
            order.put("course_datetime", input.getDate() + " " + input.getHour());
            order.put("vehicle", input.getVehicle());
            order.put("seats", ""); // Add seats info here
            orders.add(order);
        } else {
            // No network
            ui.showErrorPage("No Network Connection Available :(");
        }
    }

    public static List<JSONObject> getOrders() {
        return orders;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getOrders(Context context) throws JSONException {
        JSONArray ordersArray = new JSONArray(orders);
        connectFragment.onResponse(ordersArray.toString(), "getorders");
    }
}
