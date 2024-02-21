package com.example.pppb51_tubes02_b_f;

import java.util.ArrayList;
import java.util.HashMap;

public interface IBookFragment {
    public void updateLocationSpinner(String[] sourcesArr, HashMap<String, ArrayList<String>> payload);
    public void showErrorPage(String message);
}
