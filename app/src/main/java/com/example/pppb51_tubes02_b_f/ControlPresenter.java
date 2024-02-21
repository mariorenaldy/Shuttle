package com.example.pppb51_tubes02_b_f;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;

public class ControlPresenter {
    private IMultipleRequest connectFragment;
    private IControlActivity ui;

    public ControlPresenter(IMultipleRequest connectFragment, IControlActivity ui){
        this.connectFragment = connectFragment;
        this.ui = ui;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getRoutes(Context context){
        //Check if network is available
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if(networkInfo != null){
            GetRoutesAPI worker = new GetRoutesAPI(connectFragment, context);
            worker.execute();
        } else { //no network
            ui.showErrorPage("No Network Connection Available :(");
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getOrders(Context context) throws JSONException {
        //Check if network is available
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if(networkInfo != null){
            GetOrdersAPI worker = new GetOrdersAPI(connectFragment, context);
            worker.execute();
        } else { //no network
            ui.showErrorPage("No Network Connection Available :(");
        }
    }
}
