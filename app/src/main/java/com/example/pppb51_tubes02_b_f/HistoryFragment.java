package com.example.pppb51_tubes02_b_f;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pppb51_tubes02_b_f.databinding.FragmentHistoryBinding;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HistoryFragment extends Fragment {
    private FragmentHistoryBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater);

        getActivity().setTitle("History");

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(2).setChecked(true);

        Bundle args = getArguments();
        if (args != null){
            String response = args.getString("response");
            JSONArray json = null;
            try {
                json = new JSONArray(response);
                addHistoryItems(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return binding.getRoot();
    }
    public static HistoryFragment newInstance(String response) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString("response", response);
        fragment.setArguments(args);
        return fragment;
    }
    public void addHistoryItems(JSONArray json){
        TextView item = binding.item;

        for (int i = 0; i < json.length(); i++) {
            JSONObject obj = null;
            String source = "";
            String destination = "";
            String datetime = "";
            try {
                obj = json.getJSONObject(i);
                source = obj.getString("source");
                destination = obj.getString("destination");
                datetime = obj.getString("course_datetime");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            item.setText(item.getText()+source+" - "+destination+"  "+datetime+"\n");
        }
    }
}
