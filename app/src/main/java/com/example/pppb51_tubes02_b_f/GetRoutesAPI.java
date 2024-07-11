package com.example.pppb51_tubes02_b_f;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

public class GetRoutesAPI {
    private IMultipleRequest connectFragment;
    private Context context;

    public GetRoutesAPI(IMultipleRequest connectFragment, Context context){
        this.connectFragment = connectFragment;
        this.context = context;
    }

    public void execute(){
        // Mock data for routes
        String mockRoutesJson = "[{\"source\":\"Location A\",\"destination\":\"Location B\"},{\"source\":\"Location C\",\"destination\":\"Location D\"}]";
        try {
            JSONArray routesArray = new JSONArray(mockRoutesJson);
            processResult(routesArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            processError(e.getMessage());
        }
    }

    private void processResult(String arrayVal){
        try {
            connectFragment.onResponse(arrayVal, "getroutes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processError(String error){
        connectFragment.onError(error);
    }
}
