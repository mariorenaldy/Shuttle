package com.example.pppb51_tubes02_b_f;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;

public class LoginPresenter {
    private IConnectFragment connectFragment;
    private ILoginFragment ui;

    public LoginPresenter(IConnectFragment connectFragment, ILoginFragment ui){
        this.connectFragment = connectFragment;
        this.ui = ui;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void authenticate(Context context, String username, String password){
        //Check if network is available
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkInfo = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
        if(networkInfo != null){
            ui.setError("");
            AuthenticateAPI worker = new AuthenticateAPI(connectFragment, context);
            try {
                worker.execute(username, password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            ui.setError("No network connection available.");
        }
    }
}
