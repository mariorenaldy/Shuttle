package com.example.pppb51_tubes02_b_f;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BookPresenter{
    private IBookFragment ui;
    private IConnectFragment connectFragment;

    public BookPresenter(IConnectFragment connectFragment, IBookFragment ui){
        this.connectFragment = connectFragment;
        this.ui = ui;
    }

    public void getRoutes(Bundle args){
        //ambil hasil keluaran GetRoutesAPI yang dikirim sebagai parameter
        String arrayStr = args.getString("array");
        JSONArray array = null;
        try {
            array = new JSONArray(arrayStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //dapatkan seluruh lokasi "source"
        int size = array.length();
        HashSet<String> sources = new HashSet<String>();
        for(int i=0;i<size;i++)
        {
            try {
                sources.add(array.getJSONObject(i).getString("source"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //buat hashmap yg berisi key lokasi sources dan isi dengan value seluruh destinationnya
        HashMap<String, ArrayList<String>> payload = new HashMap<String, ArrayList<String>>();
        for (String source : sources) {
            payload.put(source, new ArrayList<>());
        }
        for(int i=0;i<size;i++)
        {
            try {
                payload.get(array.getJSONObject(i).getString("source")).add(array.getJSONObject(i).getString("destination"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //ubah hashset sources menjadi array string untuk digunakan sebagai opsi spinner
        String[] sourcesArr = sources.toArray(new String[sources.size()]);

        //set spinner From options
        ui.updateLocationSpinner(sourcesArr, payload);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getCourses(Context context, CoursesInput input) throws JSONException {
        //Check if network is available
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
