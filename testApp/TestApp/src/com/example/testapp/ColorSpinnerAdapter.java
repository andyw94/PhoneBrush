package com.example.testapp;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class ColorSpinnerAdapter extends BaseAdapter {
    ArrayList<Integer> colors;
    Context context;

    public ColorSpinnerAdapter(Context context) {
        this.context=context;
        colors=new ArrayList<Integer>();
        int retrieve []=context.getResources().getIntArray(R.array.androidcolors);
        for(int re:retrieve) {
            colors.add(re);
        }
    }
    
    @Override
    public int getCount() {
        return colors.size();
    }
    
    @Override
    public Object getItem(int arg) {
    	return colors.get(arg);
    }
    
    @Override
    public long getItemId(int arg) {
        return arg;
    }
    
    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        view=inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
        TextView txv=(TextView)view.findViewById(android.R.id.text1);
        txv.setBackgroundColor(colors.get(pos));
        txv.setTextSize(40f);
        //txv.setText("Text  " + pos);
        return view;
    }

}