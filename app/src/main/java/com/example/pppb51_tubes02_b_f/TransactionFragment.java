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

import com.example.pppb51_tubes02_b_f.databinding.FragmentTransactionBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TransactionFragment extends Fragment implements View.OnClickListener, ITransactionFragment, IMultipleRequest{
    private FragmentTransactionBinding binding;
    private HashMap<String, String> bookInfo = null;
    private TransactionPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTransactionBinding.inflate(inflater);
        getActivity().setTitle("Transaction");

        presenter = new TransactionPresenter(this, this);

        Bundle args = getArguments();
        if(args != null){
            bookInfo = (HashMap<String, String>) args.getSerializable("bookInfo");
            binding.tvFrom.setText("From: "+bookInfo.get("source"));
            binding.tvTo.setText("To: "+bookInfo.get("destination"));
            binding.tvDatetime.setText("Date & Time: "+bookInfo.get("date")+" "+bookInfo.get("hour")+":00");
            binding.tvSize.setText("Vehicle Size: "+bookInfo.get("vehicle"));
            binding.tvSeat.setText("Seat: "+bookInfo.get("seats"));
            binding.tvPrice.setText("Price per seat: "+bookInfo.get("fee"));

            int commas = 0;
            for(int i = 0; i < bookInfo.get("seats").length(); i++) {
                if(bookInfo.get("seats").charAt(i) == ',') commas++;
            }
            int price = Integer.valueOf(bookInfo.get("fee"));
            int total = price*(commas+1);
            binding.tvTotal.setText("Total: "+total);
        }

        binding.btnOrder.setOnClickListener(this);

        return binding.getRoot();
    }
    public static TransactionFragment newInstance(HashMap<String, String> bookInfo) {
        TransactionFragment fragment = new TransactionFragment();
        Bundle args = new Bundle();
        args.putSerializable("bookInfo", bookInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if (view == binding.btnOrder){
            OrderInput input = new OrderInput(bookInfo.get("course_id"), bookInfo.get("seats"));
            try {
                presenter.order(getContext(), input);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showErrorPage(String message) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ErrorFragment.newInstance(message)).commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResponse(Object response, String type) throws JSONException {
        if (type.equals("order")){
            JSONObject json = new JSONObject((String) response);
            new SweetAlertDialog(getContext())
                    .setContentText(json.getString("message"))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            try {
                                presenter.getOrders(getContext());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .show();
        }
        else{
            getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.fade_out)
                    .replace(R.id.fragment_container, HistoryFragment.newInstance((String) response)).commit();
        }
    }

    @Override
    public void onError(String error) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ErrorFragment.newInstance(error)).commit();
    }
}
