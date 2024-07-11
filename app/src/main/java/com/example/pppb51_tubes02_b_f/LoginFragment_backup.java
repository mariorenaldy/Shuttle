package com.example.pppb51_tubes02_b_f;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.pppb51_tubes02_b_f.databinding.FragmentLoginBinding;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileWriter;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginFragment_backup extends Fragment implements View.OnClickListener, IConnectFragment, ILoginFragment{
    private FragmentLoginBinding binding;
    public static String ACCESS_TOKEN = "";
    private LoginPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater);
        binding.btnLogin.setOnClickListener(this);

        ((ControlActivity)getActivity()).lockDrawer();
        ((ControlActivity)getActivity()).getSupportActionBar().hide();

        presenter = new LoginPresenter(this, this);

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if(view == binding.btnLogin){
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            String username = binding.etUsername.getText().toString();
            String password = binding.etPassword.getText().toString();
            presenter.authenticate(getContext(), username, password);
        }
    }

    @Override
    public void onResponse(Object response) {
        LoginResult result = (LoginResult) response;
        ACCESS_TOKEN = result.getToken();

        //save token to cache
        File tokenFile = new File(getContext().getCacheDir(), "token.txt");
        if(tokenFile.exists()){
            tokenFile.delete();
        }
        try {
            FileWriter writer = new FileWriter(tokenFile);
            writer.append(ACCESS_TOKEN);
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        ((TextView) getActivity().findViewById(R.id.nav_user)).setText(binding.etUsername.getText().toString());
        getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.fade_out)
                .replace(R.id.fragment_container, new HomeFragment()).commit();
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onError(String error) {
        String message = "";
        if(error.equals("E_INV_CRED")){
            message = "Invalid username \n or password!";
        }
        else if(error.equals("E_INV_USERNAME")){
            message = "Please insert a valid \n username!";
        }
        else if(error.equals("E_INV_PASSWORD")){
            message = "Please insert a valid \n password!";
        }
        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                .setContentText(message)
                .show();
    }

    @Override
    public void setError(String message) {
        binding.tvError.setText(message);
    }
}
