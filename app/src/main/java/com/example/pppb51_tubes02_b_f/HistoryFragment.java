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

        // Fetch orders from TransactionPresenter
        JSONArray orders = new JSONArray(TransactionPresenter.getOrders());
        addHistoryItems(orders);

        return binding.getRoot();
    }

    public static HistoryFragment newInstance(String response) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString("response", response);
        fragment.setArguments(args);
        return fragment;
    }

    public void addHistoryItems(JSONArray json) {
        TextView item = binding.item;
        item.setText(""); // Clear previous items

        for (int i = 0; i < json.length(); i++) {
            try {
                JSONObject obj = json.getJSONObject(i);
                String source = obj.getString("source");
                String destination = obj.getString("destination");
                String datetime = obj.getString("course_datetime");
                String vehicle = obj.getString("vehicle");
                String seats = obj.getString("seats");

                item.append(source + " - " + destination + "  " + datetime + " " + vehicle + " " + seats + "\n");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
