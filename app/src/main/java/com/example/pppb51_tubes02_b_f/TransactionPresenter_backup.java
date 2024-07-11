package com.example.pppb51_tubes02_b_f;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;

public class TransactionPresenter_backup {
    private ITransactionFragment ui;
    private IMultipleRequest connectFragment;

    public TransactionPresenter_backup(IMultipleRequest connectFragment, ITransactionFragment ui){
        this.connectFragment = connectFragment;
        this.ui = ui;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void order(Context context, OrderInput input) throws JSONException {
        //Check if network is available
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if(networkInfo != null){
            AddNewOrderAPI worker = new AddNewOrderAPI(connectFragment, context);
            worker.execute(input);
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
