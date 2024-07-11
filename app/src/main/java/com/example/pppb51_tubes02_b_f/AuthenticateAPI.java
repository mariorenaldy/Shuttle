package com.example.pppb51_tubes02_b_f;

import android.content.Context;

import com.google.gson.Gson;

public class AuthenticateAPI {
    private IConnectFragment connectFragment;
    private Context context;
    private Gson gson;
    private String userId;

    public AuthenticateAPI(IConnectFragment connectFragment, Context context) {
        this.connectFragment = connectFragment;
        this.context = context;
    }

    public void execute(String username, String password) {
        // Mock response
        String mockJsonResponse;
        if ((username.equals("user1") && password.equals("user1")) || (username.equals("user2") && password.equals("user2"))) {
            mockJsonResponse = "{ \"success\": true, \"token\": \"mock_token\", \"userId\": \"" + username + "\", \"error\": \"\" }";
        } else {
            mockJsonResponse = "{ \"success\": false, \"token\": \"\", \"userId\": \"\", \"error\": \"E_INV_CRED\" }";
        }
        processResult(mockJsonResponse);
    }

    private void processResult(String json) {
        gson = new Gson();
        LoginResult result = gson.fromJson(json, LoginResult.class);
        if (result.isSuccess()) {
            userId = result.getUserId();
            connectFragment.onResponse(result);
        } else {
            processError(result.getError());
        }
    }

    private void processError(String error) {
        connectFragment.onError(error);
    }

    public String getUserId() {
        return userId;
    }
}