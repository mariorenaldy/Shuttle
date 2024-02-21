package com.example.pppb51_tubes02_b_f;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.pppb51_tubes02_b_f.databinding.FragmentLargeSeatBinding;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LargeSeatFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, ILargeSeatFragment, IConnectFragment {
    private FragmentLargeSeatBinding binding;
    private Bitmap mBitmap;
    private ImageView ivCanvas;
    private Canvas mCanvas;
    private int maxWidth;
    private int maxHeight;
    private ArrayList<Seat> seats;
    private Paint paint;
    private LargeSeatPresenter presenter;
    private HashMap<String, String> bookInfo;
    private ArrayList<String> occupied;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLargeSeatBinding.inflate(inflater);
        getActivity().setTitle("Choose Seat");

        binding.btnSwitchSize.setOnClickListener(this);
        binding.btnProceed.setOnClickListener(this);

        presenter = new LargeSeatPresenter(this, this);
        Sensey.getInstance().init(getContext(), Sensey.SAMPLING_PERIOD_UI);
        ShakeDetector.ShakeListener shakeListener=new ShakeDetector.ShakeListener() {
            @Override public void onShakeDetected() {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onShakeStopped() {
                switchSize();
            }

        };

        Sensey.getInstance().startShakeDetection(shakeListener);

        this.ivCanvas = binding.ivCanvas;

        Bundle args = getArguments();
        if (args != null){
            bookInfo = (HashMap<String, String>) args.getSerializable("bookInfo");
            String str = bookInfo.get("occupied");
            occupied = new ArrayList<>(Arrays.asList(str.split(",")));
        }

        binding.rlRoot.post(new Runnable() {
            @Override
            public void run() {
                drawCanvas();
            }
        });

        ivCanvas.setOnTouchListener(this);
        return binding.getRoot();
    }
    public static LargeSeatFragment newInstance(HashMap<String,String> bookInfo) {
        LargeSeatFragment fragment = new LargeSeatFragment();
        Bundle args = new Bundle();
        args.putSerializable("bookInfo", bookInfo);
        fragment.setArguments(args);
        return fragment;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if (view == binding.btnSwitchSize){
            switchSize();
        }
        else if (view == binding.btnProceed){
            bookInfo = (HashMap<String, String>) getArguments().getSerializable("bookInfo");
            bookInfo.put("seats","");
            for (Seat seat : seats) {
                if(seat.isSelected()){
                    if(bookInfo.get("seats").equals("")){
                        bookInfo.put("seats", bookInfo.get("seats").concat(seat.getNumber()));
                    }
                    else{
                        bookInfo.put("seats", bookInfo.get("seats").concat(","+seat.getNumber()));
                    }
                }
            }
            if(bookInfo.get("seats").equals("")){
                new SweetAlertDialog(getContext())
                        .setContentText("Please select a seat")
                        .show();
            }
            else{
                getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.slide_out)
                        .replace(R.id.fragment_container, TransactionFragment.newInstance(bookInfo)).commit();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void switchSize(){
        bookInfo = (HashMap<String, String>) getArguments().getSerializable("bookInfo");
        bookInfo.put("vehicle", "Small");
        CoursesInput input = new CoursesInput(bookInfo.get("source"), bookInfo.get("destination"), bookInfo.get("vehicle"), bookInfo.get("date"), bookInfo.get("hour"));
        try {
            presenter.getCourses(getContext(), input);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float touchX = motionEvent.getX();
        float touchY = motionEvent.getY();
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            for (Seat seat : seats) {
                if(seat.isSelectable()){
                    if (seat.getRectangle().contains(touchX, touchY)) {
                        if (!seat.isSelected()) {
                            selectSeat(seat);
                        } else {
                            deselectSeat(seat);
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void showErrorPage(String message) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ErrorFragment.newInstance(message)).commit();
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
        getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.fade_out)
                .replace(R.id.fragment_container, SmallSeatFragment.newInstance(bookInfo)).commit();
    }

    @Override
    public void onError(String error) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ErrorFragment.newInstance(error)).commit();
    }
    public void drawCanvas(){
        maxWidth = ivCanvas.getMeasuredWidth();
        maxHeight = ivCanvas.getMeasuredHeight();
        mBitmap = Bitmap.createBitmap(maxWidth,maxHeight,Bitmap.Config.ARGB_8888);
        ivCanvas.setImageBitmap(mBitmap);

        mCanvas = new Canvas(mBitmap);

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(maxWidth*0.005f);
        paint.setColor(Color.BLACK);

        Paint paint2 = new Paint();
        paint2.setColor(Color.parseColor("#f8cecc"));

        RectF first = new RectF(maxWidth*0.20f, maxHeight*0.05f, maxWidth*0.35f, maxHeight*0.20f);
        Seat one = null;
        if (occupied.contains("1")){
            one = new Seat("1", first, false);
            mCanvas.drawRect(first, paint2);
        }
        else{
            one = new Seat("1", first);
        }
        mCanvas.drawRect(first, paint);
        paint.setTextSize(first.width()*0.5f);
        mCanvas.drawText("1", first.centerX(), first.centerY(), paint);

        RectF second = new RectF(maxWidth*0.65f, maxHeight*0.05f, maxWidth*0.80f, maxHeight*0.20f);
        Seat two = null;
        if (occupied.contains("2")){
            two = new Seat("2", second, false);
            mCanvas.drawRect(second, paint2);
        }
        else{
            two = new Seat("2", second);
        }
        mCanvas.drawRect(second, paint);
        mCanvas.drawText("2", second.centerX(), second.centerY(), paint);

        RectF third = new RectF(maxWidth*0.20f, maxHeight*0.25f, maxWidth*0.35f, maxHeight*0.40f);
        Seat three = null;
        if (occupied.contains("3")){
            three = new Seat("3", third, false);
            mCanvas.drawRect(third, paint2);
        }
        else{
            three = new Seat("3", third);
        }
        mCanvas.drawRect(third, paint);
        mCanvas.drawText("3", third.centerX(), third.centerY(), paint);

        RectF fourth = new RectF(maxWidth*0.65f, maxHeight*0.25f, maxWidth*0.80f, maxHeight*0.40f);
        Seat four = null;
        if (occupied.contains("4")){
            four = new Seat("4", fourth, false);
            mCanvas.drawRect(fourth, paint2);
        }
        else{
            four = new Seat("4", fourth);
        }
        mCanvas.drawRect(fourth, paint);
        mCanvas.drawText("4", fourth.centerX(), fourth.centerY(), paint);

        RectF fifth = new RectF(maxWidth*0.20f, maxHeight*0.45f, maxWidth*0.35f, maxHeight*0.60f);
        Seat five = null;
        if (occupied.contains("5")){
            five = new Seat("5", fifth, false);
            mCanvas.drawRect(fifth, paint2);
        }
        else{
            five = new Seat("5", fifth);
        }
        mCanvas.drawRect(fifth, paint);
        mCanvas.drawText("5", fifth.centerX(), fifth.centerY(), paint);

        RectF sixth = new RectF(maxWidth*0.65f, maxHeight*0.45f, maxWidth*0.80f, maxHeight*0.60f);
        Seat six = null;
        if (occupied.contains("6")){
            six = new Seat("6", sixth, false);
            mCanvas.drawRect(sixth, paint2);
        }
        else{
            six = new Seat("6", sixth);
        }
        mCanvas.drawRect(sixth, paint);
        mCanvas.drawText("6", sixth.centerX(), sixth.centerY(), paint);

        RectF seventh = new RectF(maxWidth*0.20f, maxHeight*0.65f, maxWidth*0.35f, maxHeight*0.80f);
        Seat seven = null;
        if (occupied.contains("7")){
            seven = new Seat("7", seventh, false);
            mCanvas.drawRect(seventh, paint2);
        }
        else{
            seven = new Seat("7", seventh);
        }
        mCanvas.drawRect(seventh, paint);
        mCanvas.drawText("7", seventh.centerX(), seventh.centerY(), paint);

        RectF eighth = new RectF(maxWidth*0.65f, maxHeight*0.65f, maxWidth*0.80f, maxHeight*0.80f);
        Seat eight = null;
        if (occupied.contains("8")){
            eight = new Seat("8", eighth, false);
            mCanvas.drawRect(eighth, paint2);
        }
        else{
            eight = new Seat("8", eighth);
        }
        mCanvas.drawRect(eighth, paint);
        mCanvas.drawText("8", eighth.centerX(), eighth.centerY(), paint);

        RectF ninth = new RectF(maxWidth*0.20f, maxHeight*0.85f, maxWidth*0.35f, maxHeight);
        Seat nine = null;
        if (occupied.contains("9")){
            nine = new Seat("9", ninth, false);
            mCanvas.drawRect(ninth, paint2);
        }
        else{
            nine = new Seat("9", ninth);
        }
        mCanvas.drawRect(ninth, paint);
        mCanvas.drawText("9", ninth.centerX(), ninth.centerY(), paint);

        RectF tenth = new RectF(maxWidth*0.65f, maxHeight*0.85f, maxWidth*0.80f, maxHeight);
        Seat ten = null;
        if (occupied.contains("10")){
            ten = new Seat("10", tenth, false);
            mCanvas.drawRect(tenth, paint2);
        }
        else{
            ten = new Seat("10", tenth);
        }
        mCanvas.drawRect(tenth, paint);
        mCanvas.drawText("10", tenth.centerX()-tenth.centerX()*0.05f, tenth.centerY(), paint);

        seats = new ArrayList<Seat>();
        seats.add(one);
        seats.add(two);
        seats.add(three);
        seats.add(four);
        seats.add(five);
        seats.add(six);
        seats.add(seven);
        seats.add(eight);
        seats.add(nine);
        seats.add(ten);
    }
    public void selectSeat(Seat seat){
        seat.setSelected(true);
        Paint paint2 = new Paint();
        paint2.setColor(Color.parseColor("#fff2cc"));

        RectF seatRect = seat.getRectangle();
        mCanvas.drawRect(seatRect, paint2);

        paint2.setColor(Color.parseColor("#f3e1aa"));
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(maxWidth*0.005f);
        mCanvas.drawRect(seatRect, paint2);

        if(seat.getNumber().equals("10")){
            mCanvas.drawText(seat.getNumber(), seatRect.centerX()-seatRect.centerX()*0.05f, seatRect.centerY(), paint);
        }
        else{
            mCanvas.drawText(seat.getNumber(), seatRect.centerX(), seatRect.centerY(), paint);
        }
        ivCanvas.invalidate();
    }
    public void deselectSeat(Seat seat){
        seat.setSelected(false);
        Paint paint2 = new Paint();
        paint2.setColor(Color.WHITE);

        RectF seatRect = seat.getRectangle();
        mCanvas.drawRect(seatRect, paint2);
        mCanvas.drawRect(seatRect, paint);

        if(seat.getNumber().equals("10")){
            mCanvas.drawText(seat.getNumber(), seatRect.centerX()-seatRect.centerX()*0.05f, seatRect.centerY(), paint);
        }
        else{
            mCanvas.drawText(seat.getNumber(), seatRect.centerX(), seatRect.centerY(), paint);
        }
        ivCanvas.invalidate();
    }
}
