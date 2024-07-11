package com.example.pppb51_tubes02_b_f;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class OrderStorage {
    private static final String PREFS_NAME = "BookedSeats";
    private static final String KEY_BOOKINGS = "bookings";

    private SharedPreferences sharedPreferences;

    public OrderStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveBooking(String source, String destination, String date, String time, String vehicle, String seats) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray bookingsArray;

        String existingBookings = sharedPreferences.getString(KEY_BOOKINGS, "");
        if (!existingBookings.isEmpty()) {
            try {
                bookingsArray = new JSONArray(existingBookings);
            } catch (JSONException e) {
                bookingsArray = new JSONArray();
            }
        } else {
            bookingsArray = new JSONArray();
        }

        JSONObject newBooking = new JSONObject();
        try {
            newBooking.put("source", source);
            newBooking.put("destination", destination);
            newBooking.put("date", date);
            newBooking.put("time", time);
            newBooking.put("vehicle", vehicle);
            newBooking.put("seats", seats);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bookingsArray.put(newBooking);
        editor.putString(KEY_BOOKINGS, bookingsArray.toString());
        editor.apply();
    }

    public Set<String> getBookedSeats(String source, String destination, String date, String time, String vehicle) {
        Set<String> bookedSeats = new HashSet<>();
        String existingBookings = sharedPreferences.getString(KEY_BOOKINGS, "");

        if (!existingBookings.isEmpty()) {
            try {
                JSONArray bookingsArray = new JSONArray(existingBookings);
                for (int i = 0; i < bookingsArray.length(); i++) {
                    JSONObject booking = bookingsArray.getJSONObject(i);
                    if (booking.getString("source").equals(source) &&
                            booking.getString("destination").equals(destination) &&
                            booking.getString("date").equals(date) &&
                            booking.getString("time").equals(time) &&
                            booking.getString("vehicle").equals(vehicle)) {

                        String seats = booking.getString("seats");
                        String[] seatsArray = seats.split(",");
                        for (String seat : seatsArray) {
                            bookedSeats.add(seat);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return bookedSeats;
    }
}
