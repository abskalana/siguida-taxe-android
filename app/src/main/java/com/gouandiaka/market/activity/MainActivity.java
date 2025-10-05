package com.gouandiaka.market.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.gouandiaka.market.HttpHelper;
import com.gouandiaka.market.LocalDatabase;
import com.gouandiaka.market.R;
import com.gouandiaka.market.WaitingView;
import com.gouandiaka.market.utils.LocationUtils;
import com.gouandiaka.market.utils.PrefUtils;
import com.gouandiaka.market.utils.RequestListener;
import com.gouandiaka.market.utils.Utils;

import java.util.Calendar;

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
                if(Utils.shoulRequestConfig()){
                    Utils.launchConfigActivity(MainActivity.this);
                    return;
                }

                String annee = PrefUtils.getString("annee", String.valueOf(defaultYear));
                String mois = PrefUtils.getString("mois");
                if(Utils.isEmpty(annee) || Utils.isSelectOrEmpty(mois)) {
                    Toast.makeText(MainActivity.this,"Specifier l'année et le mois Config",Toast.LENGTH_SHORT).show();
                    return;
                }

                String property = PrefUtils.getString("espace", "PRIVEE");
                String locality = PrefUtils.getString("place");
                if(Utils.isSelectOrEmpty(property) || Utils.isSelectOrEmpty(locality)) {
                    Toast.makeText(MainActivity.this,"Specifier le type propriete et le quartier Config",Toast.LENGTH_SHORT).show();
                    return;
                }

                waitingView.start();
                HttpHelper.loadEntity(annee, mois,property, locality,MainActivity.this);
            }
        });

        findViewById(R.id.ic_menu_envoyer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waitingView.start();
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
