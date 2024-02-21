package com.example.pppb51_tubes02_b_f;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pppb51_tubes02_b_f.databinding.FragmentErrorBinding;
import com.google.android.material.navigation.NavigationView;

public class ErrorFragment extends Fragment {
    private FragmentErrorBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentErrorBinding.inflate(inflater);

        ((ControlActivity)getActivity()).unlockDrawer();
        ((ControlActivity)getActivity()).getSupportActionBar().show();

        getActivity().setTitle("Error");

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }

        Bundle args = getArguments();
        String errorMessage = args.getString("error");

        binding.tvError.setText(errorMessage);

        return binding.getRoot();
    }
    public static ErrorFragment newInstance(String errorMessage) {
        ErrorFragment fragment = new ErrorFragment();
        Bundle args = new Bundle();
        args.putString("error", errorMessage);
        fragment.setArguments(args);
        return fragment;
    }
}
