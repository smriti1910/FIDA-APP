package com.example.safetyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class EmergencyGridAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> emergencyList;
    private final int[] imageIds;

    public EmergencyGridAdapter(Context context, List<String> emergencyList, int[] imageIds) {
        super(context, R.layout.emergency_grid_item, emergencyList);
        this.context = context;
        this.emergencyList = emergencyList;
        this.imageIds = imageIds;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.emergency_grid_item, null);
        }

        TextView emergencyText = rowView.findViewById(R.id.emergencyText);
        ImageView emergencyImage = rowView.findViewById(R.id.emergencyImage);

        emergencyText.setText(emergencyList.get(position));
        emergencyImage.setImageResource(imageIds[position]);

        return rowView;
    }
}
