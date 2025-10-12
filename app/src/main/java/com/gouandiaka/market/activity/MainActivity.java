package com.gouandiaka.market.activity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gouandiaka.market.R;
import com.gouandiaka.market.data.HttpHelper;
import com.gouandiaka.market.data.LocalDatabase;
import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.utils.LocationUtils;
import com.gouandiaka.market.utils.RequestListener;
import com.gouandiaka.market.utils.Utils;
import com.gouandiaka.market.view.WaitingView;

import java.util.Calendar;

public class MainActivity extends BaseActivity implements RequestListener  {

    private WaitingView waitingView;
    private TextView charger;
    private TextView paiement;
    private TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        charger = findViewById(R.id.ic_menu_charger_count);
        paiement = findViewById(R.id.label_paiement);
        waitingView = findViewById(R.id.waiting_view);
        save = findViewById(R.id.menu_enregistrer);
        int defaultYear = Calendar.getInstance().get(Calendar.YEAR);
        findViewById(R.id.menu_gps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationUtils.setupPermission(MainActivity.this);
            }
        });

        findViewById(R.id.menu_entity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.shoulRequestConfig()){
                    Utils.launchConfigActivity(MainActivity.this);
                    return;
                }
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
                if(Utils.shoulRequestConfig()){
                    Utils.launchConfigActivity(MainActivity.this);
                    return;
                }
                Utils.launchPaiementActivity(MainActivity.this);
            }
        });

        findViewById(R.id.ic_menu_charger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waitingView.start(MainActivity.this);
                HttpHelper.reloadEntity(MainActivity.this,MainActivity.this);
            }
        });

        findViewById(R.id.ic_menu_envoyer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waitingView.start(MainActivity.this);
                HttpHelper.sendAll(new RequestListener() {
                    @Override
                    public void onSuccess(boolean b, Entity entityList) {
                        if(b){
                            LocalDatabase.instance().clearLocaldata();
                            showState();
                        }
                        waitingView.stop(b,MainActivity.this);
                    }
                });
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



    }
    private void showState(){
        int c = LocalDatabase.instance().getLocaleModelCount();
        save.setText("Enregistrer (" + c + " )");
        c = LocalDatabase.instance().getRemoteModelCount();
        charger.setText("Charger (" + c + " )");
        c = LocalDatabase.instance().getLocalPaiementCount();
        paiement.setText("Paiement (" + c + " )");
        c = LocalDatabase.instance().getRemoteModelCount();
        charger.setText("Charger (" + c + " )");
    }



    @Override
    public void onSuccess(boolean b,Entity entities) {
        waitingView.stop(b,MainActivity.this);
        if(b){
            Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show();
        }
        showState();
    }

    @Override
    public void onLocationChanged( Location locations) {
        super.onLocationChanged(locations);

    }

}
