package com.gouandiaka.market.entity;


import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gouandiaka.market.R;


public class PlacesItemView extends LinearLayout {

    private final TextView name;


    public PlacesItemView(Context context, String itemName, String itemType, String itemLocality) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_item_place, this, true);
        name = (TextView) findViewById(R.id.tv_taxi_name);
        reuse(itemName, itemType, itemLocality);

    }

    public void reuse(final String itemName, String itemType, String itemLocality) {
        name.setText(itemName.toUpperCase() + " " + itemType + " Tel:" + itemLocality);

    }
}
