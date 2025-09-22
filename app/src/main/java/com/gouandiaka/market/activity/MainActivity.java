package com.gouandiaka.market.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.gouandiaka.market.HttpHelper;
import com.gouandiaka.market.utils.LocationUtils;
import com.gouandiaka.market.R;
import com.gouandiaka.market.utils.Utils;

public class MainActivity extends BaseActivity {

    private View waitingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        waitingView = findViewById(R.id.waiting_view);
        findViewById(R.id.menu_gps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationUtils.setupPermission(MainActivity.this);
            }
        });

        findViewById(R.id.menu_entity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Utils.launchEnregistrementActivity(MainActivity.this);
            }
        });
        findViewById(R.id.menu_commune).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.launchConfigActivity(MainActivity.this);
            }
        });
        findViewById(R.id.menu_paiement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.launchPaiementActivity(MainActivity.this);
            }
        });

        findViewById(R.id.ic_menu_charger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpHelper.syncEntity(MainActivity.this,waitingView);
            }
        });

        findViewById(R.id.ic_menu_envoyer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 setupNetwork(MainActivity.this,waitingView);
            }
        });

    }

    private  void  setupNetwork(Context context, View waitingView) {
        waitingView.setVisibility(View.VISIBLE);
        new Thread(() -> {
            String response = HttpHelper.makeRequest();
            if (response == null) {
                runOnUiThread(() -> {
                    waitingView.setVisibility(View.GONE);
                    Toast.makeText(context, "ECHEC ", Toast.LENGTH_SHORT).show();
                });
            } else {
                runOnUiThread(() -> {
                    waitingView.setVisibility(View.GONE);
                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

}
