package com.gouandiaka.market.entity;


import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gouandiaka.market.R;
import com.gouandiaka.market.utils.Utils;


public class PlacesItemView extends LinearLayout {

    private final TextView name, paiement;


    public PlacesItemView(Context context, String itemName, String itemType, String itemLocality,String status) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_item_place, this, true);
        name = (TextView) findViewById(R.id.tv_taxi_name);
        paiement = findViewById(R.id.tv_tatus);
        reuse(itemName, itemType, itemLocality,status);

    }

    public void reuse(final String itemName, String itemType, String itemLocality, String status) {
        name.setText(itemName.toUpperCase() + " " + itemType + " Tel:" + itemLocality );
        paiement.setText(Utils.getColoredStatus(status));
    }
}
