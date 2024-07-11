package com.example.pppb51_tubes02_b_f;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class BookedSeatsManager {
    private static final String PREFS_NAME = "BookedSeats";
    private static final String KEY_BOOKINGS = "bookings";

    public static void saveBookedSeats(Context context, String tripKey, Set<String> seats) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(tripKey, seats);
        editor.apply();
    }

    public static Set<String> loadBookedSeats(Context context, String tripKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(tripKey, new HashSet<>());
    }

    public static String generateTripKey(String source, String destination, String date, String time, String vehicle) {
        return source + "_" + destination + "_" + date + "_" + time + "_" + vehicle;
    }
}