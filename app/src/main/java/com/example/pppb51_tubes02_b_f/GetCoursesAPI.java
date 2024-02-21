package com.example.pppb51_tubes02_b_f;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class GetCoursesAPI {
    private String BASE_URL = "https://devel.loconode.com/pppb/v1/courses";
    private IConnectFragment connectFragment;
    private Context context;

    public GetCoursesAPI(IConnectFragment connectFragment, Context context){
        this.connectFragment = connectFragment;
        this.context = context;
    }
    public void execute(CoursesInput input) throws JSONException {
        BASE_URL += "?source="+input.getSource()+"&destination="+input.getDestination()+"&vehicle="+input.getVehicle()+"&date="+input.getDate()+"&hour="+input.getHour();
        callVolley();
    }
    private void callVolley(){
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray arrayVal = null;
                try {
                    arrayVal = response.getJSONArray("payload");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                processResult(arrayVal.toString());
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
                    processError(body);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String auth = "Bearer " + LoginFragment.ACCESS_TOKEN;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }
    private void processResult(String arrayVal){
        connectFragment.onResponse(arrayVal);
    }
    private void processError(String error){
        connectFragment.onError(error);
    }
}
