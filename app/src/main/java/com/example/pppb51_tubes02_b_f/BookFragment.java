package com.example.pppb51_tubes02_b_f;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.example.pppb51_tubes02_b_f.databinding.FragmentBookBinding;
import com.google.android.material.navigation.NavigationView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BookFragment extends Fragment implements IBookFragment, IConnectFragment, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, View.OnClickListener {
    private FragmentBookBinding binding;
    private BookPresenter presenter;
    private HashMap<String, String> bookInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBookBinding.inflate(inflater);

        ((ControlActivity)getActivity()).unlockDrawer();
        ((ControlActivity)getActivity()).getSupportActionBar().show();

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);

        getActivity().setTitle("Book a Seat");

        //ambil hasil keluaran GetRoutesAPI yang dikirim sebagai parameter
        Bundle args = getArguments();
        this.presenter = new BookPresenter(this, this);
        presenter.getRoutes(args);
        setSizeSpinner();

        return binding.getRoot();
    }
    public static BookFragment newInstance(String arrayVal) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        args.putString("array", arrayVal);
        fragment.setArguments(args);
        return fragment;
    }
    public void setSizeSpinner(){
        //set spinner Vehicle Size options
        String vehicle_sizes[] = {"Large","Small"};
        Spinner vehicleSizeSpinner = binding.spUkuran;
        ArrayAdapter<String> vehicleArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, vehicle_sizes);
        vehicleArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        vehicleSizeSpinner.setAdapter(vehicleArrayAdapter);

        binding.btnTanggal.setOnClickListener(this);
        binding.btnWaktu.setOnClickListener(this);
        binding.btnCari.setOnClickListener(this);
    }
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String time = "";
        if(hourOfDay<10){
            time = "0"+hourOfDay;
        }
        else{
            time += hourOfDay;
        }
        binding.tVPilihanWaktu.setText(time);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"-"+(monthOfYear+1)+"-"+year;
        binding.tVPilihanTanggal.setText(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if (view == binding.btnTanggal){
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    BookFragment.this,
                    now.get(Calendar.YEAR), // Initial year selection
                    now.get(Calendar.MONTH), // Initial month selection
                    now.get(Calendar.DAY_OF_MONTH) // Inital day selection
            );
            dpd.setAccentColor(0xFF758bd9);
            dpd.show(getParentFragmentManager(), "Datepickerdialog");
        }
        else if (view == binding.btnWaktu){
            TimePickerDialog tpd = TimePickerDialog.newInstance(BookFragment.this, false
            );
            tpd.setAccentColor(0xFF758bd9);
            tpd.enableMinutes(false);
            tpd.show(getParentFragmentManager(), "Timepickerdialog");
        }
        else if (view == binding.btnCari){
            if (binding.tVPilihanTanggal.getText().toString().length()==0 && binding.tVPilihanWaktu.getText().toString().length()==0){
                new SweetAlertDialog(getContext())
                        .setContentText("Please select a date and time")
                        .show();
            }
            else if (binding.tVPilihanTanggal.getText().toString().length()==0 || binding.tVPilihanWaktu.getText().toString().length()==0){
                if (binding.tVPilihanTanggal.getText().toString().length()==0){
                    new SweetAlertDialog(getContext())
                            .setContentText("Please select a date")
                            .show();
                }
                else{
                    new SweetAlertDialog(getContext())
                            .setContentText("Please select a time")
                            .show();
                }
            }
            else{
                bookInfo = new HashMap<String, String>();
                bookInfo.put("source", (String) binding.spDari.getSelectedItem());
                bookInfo.put("destination", (String) binding.spKe.getSelectedItem());
                bookInfo.put("date", binding.tVPilihanTanggal.getText().toString());
                bookInfo.put("hour", binding.tVPilihanWaktu.getText().toString());
                bookInfo.put("vehicle", (String) binding.spUkuran.getSelectedItem());
                CoursesInput input = new CoursesInput(bookInfo.get("source"), bookInfo.get("destination"), bookInfo.get("vehicle"), bookInfo.get("date"), bookInfo.get("hour"));
                try {
                    presenter.getCourses(getContext(), input);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void updateLocationSpinner(String[] sourcesArr, HashMap<String, ArrayList<String>> payload) {
        Spinner spinner = binding.spDari;
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sourcesArr);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3)
            {
                String selected = parent.getItemAtPosition(position).toString();

                //set spinner To options
                Spinner spinner2 = binding.spKe;
                ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, payload.get(selected));
                spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner2.setAdapter(spinnerArrayAdapter2);
            }

            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });
    }
    @Override
    public void onResponse(Object response) {
        JSONArray json = null;
        try {
            json = new JSONArray((String) response);
            JSONObject obj = json.getJSONObject(0);
            bookInfo.put("fee", obj.getString("fee"));
            bookInfo.put("course_id", obj.getString("course_id"));
            JSONArray arr = new JSONArray(obj.getString("seats"));
            String occupied = "";
            for(int i = 0; i < arr.length(); i++){
                if(i==0){
                    occupied+=arr.get(i);
                }
                else{
                    occupied+=","+arr.get(i);
                }
            }
            bookInfo.put("occupied", occupied);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (binding.spUkuran.getSelectedItem().equals("Large")){
            getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.fade_out)
                    .replace(R.id.fragment_container, LargeSeatFragment.newInstance(bookInfo)).commit();
        }
        else{
            getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.fade_out)
                    .replace(R.id.fragment_container, SmallSeatFragment.newInstance(bookInfo)).commit();
        }
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
