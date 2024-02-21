package com.example.pppb51_tubes02_b_f;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.pppb51_tubes02_b_f.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements View.OnClickListener, IMultipleRequest, IHomeFragment{
    private FragmentHomeBinding binding;
    private HomePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);
        binding.btnBookSeat.setEnabled(true);
        binding.btnBookSeat.setOnClickListener(this);

        ((ControlActivity)getActivity()).lockDrawer();
        ((ControlActivity)getActivity()).getSupportActionBar().hide();

        presenter = new HomePresenter(this, this);

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if (view == binding.btnBookSeat){
            binding.btnBookSeat.setEnabled(false);
            presenter.getRoutes(getContext());
        }
    }

    @Override
    public void onResponse(Object response, String type) {
        getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.fragment_container, BookFragment.newInstance((String) response)).commit();
    }

    @Override
    public void onError(String error) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ErrorFragment.newInstance(error)).commit();
    }

    @Override
    public void showErrorPage(String message) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ErrorFragment.newInstance(message)).commit();
    }
}
