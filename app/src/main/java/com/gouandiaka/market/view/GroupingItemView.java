package com.gouandiaka.market.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gouandiaka.market.R;

@SuppressLint("ViewConstructor")
public class GroupingItemView extends LinearLayout {

    private final TextView name;

    public GroupingItemView(Context context, String area, int size) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_item_place_quartier, this, true);
        name = findViewById(R.id.tv_area_name);
        reuse(area, size);
    }

    public void reuse(final String s, int size) {
        name.setText(s + "  ("+ size +") ");
    }
}
