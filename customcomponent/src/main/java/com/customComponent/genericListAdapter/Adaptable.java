package com.customComponent.genericListAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Anand on 06-05-2016.
 */
public interface Adaptable {

    public View buildView(View v, LayoutInflater inflater, ViewGroup parent);
}
