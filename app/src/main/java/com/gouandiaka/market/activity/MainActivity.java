package com.gouandiaka.market.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gouandiaka.market.HttpHelper;
import com.gouandiaka.market.LocalDatabase;
import com.gouandiaka.market.R;
import com.gouandiaka.market.WaitingView;
import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.entity.Paiement;
import com.gouandiaka.market.utils.LocationUtils;
import com.gouandiaka.market.utils.RequestListener;
import com.gouandiaka.market.utils.Utils;

import java.util.List;

public class MainActivity extends BaseActivity implements RequestListener  {

    private WaitingView waitingView;
    private TextView charger;
    private TextView paiement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        charger = findViewById(R.id.ic_menu_charger_count);
        paiement = findViewById(R.id.label_paiement);
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
                HttpHelper.loadEntity(MainActivity.this, waitingView, null);
            }
        });

        findViewById(R.id.ic_menu_envoyer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpHelper.sendAll(MainActivity.this,MainActivity.this);
            }
        });
        boolean showConfig = getIntent().getBooleanExtra("SHOW_CONFIG", false);
       if (showConfig) {
           Utils.launchConfigActivity(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView textView = findViewById(R.id.menu_enregistrer);
        int c = LocalDatabase.instance().getLocaleModelCount();
        textView.setText("Enregistrer (" + c + " )");
        c = LocalDatabase.instance().getRemoteModelCount();
        charger.setText("Charger (" + c + " )");
        c = LocalDatabase.instance().getLocalPaiementCount();
        paiement.setText("Paiement (" + c + " )");



    }

    @Override
    public void onSuccess(boolean b) {
        waitingView.stop(b);
        if(b){
            Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show();
        }
    }

}
