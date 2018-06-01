package com.customComponent.genericListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Anand on 06-05-2016.
 */
public class GenericAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List items;
    @SuppressWarnings("unchecked")
    public GenericAdapter(List items, Context c) {
        this.items = (List) items;
        inflater = LayoutInflater.from(c);
    }
    @Override
    public int getCount() {
        return items.size();
    }
    @Override
    public Object getItem(int position) {
        return items.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
        //return items.get(position).buildView(convertView, inflater, parent); }
}
