package com.example.pppb51_tubes02_b_f;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

public class AuthenticateAPI_backup {
    private static final String BASE_URL = "https://devel.loconode.com/pppb/v1/authenticate";
    private IConnectFragment connectFragment;
    private Context context;
    private Gson gson;

    public AuthenticateAPI_backup(IConnectFragment connectFragment, Context context){
        this.connectFragment = connectFragment;
        this.context = context;
    }
    public void execute(String username, String password) throws JSONException {
        LoginInput input = new LoginInput(username, password);
        gson = new Gson();
        String strInput = gson.toJson(input);
        JSONObject json = null;
        json = new JSONObject(strInput);
        callVolley(json);
    }
    private void callVolley(JSONObject json){
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                processResult(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null || error.networkResponse != null) {
                    String body = null;
                    final String statusCode = String.valueOf(error.networkResponse.statusCode);
                    try {
                        body = new String(error.networkResponse.data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {

                    }
                    JSONObject errcode = null;
                    try {
                        errcode = new JSONObject(body);
                        processError(errcode.getString("errcode"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        queue.add(jsonObjectRequest);
    }
    private void processResult(String json){
        LoginResult result = gson.fromJson(json, LoginResult.class);
        connectFragment.onResponse(result);
    }
    private void processError(String error){
        connectFragment.onError(error);
    }
}
