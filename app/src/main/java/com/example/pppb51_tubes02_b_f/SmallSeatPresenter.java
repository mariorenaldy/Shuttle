package com.example.pppb51_tubes02_b_f;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;

public class SmallSeatPresenter {
    private ISmallSeatFragment ui;
    private IConnectFragment connectFragment;

    public SmallSeatPresenter(IConnectFragment connectFragment, ISmallSeatFragment ui){
        this.connectFragment = connectFragment;
        this.ui = ui;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getCourses(Context context, CoursesInput input) throws JSONException {
        //Check if network is available
        if(context != null){
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
            if(networkInfo != null){
                GetCoursesAPI worker = new GetCoursesAPI(connectFragment, context);
                worker.execute(input);
            } else { //no network
                ui.showErrorPage("No Network Connection Available :(");
            }
        }
    }
}
