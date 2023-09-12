package com.cogostech.simreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


   class DualSimView extends ArrayAdapter<SimInfo> {

    public DualSimView(Context context, ArrayList<SimInfo> items) {
        super(context, R.layout.simci, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SimInfo telInf = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simci, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.SIMfirstLine);
        TextView tvHome = (TextView) convertView.findViewById(R.id.SIMsecondLine);
        // Populate the data into the template view using the data object
        tvName.setText(telInf.s1);
        tvHome.setText(telInf.s2);
        // Return the completed view to render on screen
        return convertView;
        //return super.getView(position, convertView, parent);
    }
}
