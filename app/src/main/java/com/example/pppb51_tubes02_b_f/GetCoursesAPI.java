package com.example.pppb51_tubes02_b_f;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetCoursesAPI {
    private IConnectFragment connectFragment;
    private Context context;

    public GetCoursesAPI(IConnectFragment connectFragment, Context context){
        this.connectFragment = connectFragment;
        this.context = context;
    }

    public void execute(CoursesInput input) {
        // Mock data for courses with fees in Indonesian Rupiah
        String mockCoursesJson = "[{\"course_id\":\"course_1\",\"fee\":\"100000\",\"seats\":\"[1, 2, 3]\"},{\"course_id\":\"course_2\",\"fee\":\"200000\",\"seats\":\"[4, 5, 6]\"}]";
        try {
            JSONArray coursesArray = new JSONArray(mockCoursesJson);
            processResult(coursesArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            processError(e.getMessage());
        }
    }

    private void processResult(String arrayVal){
        connectFragment.onResponse(arrayVal);
    }

    private void processError(String error){
        connectFragment.onError(error);
    }
}
