package com.gouandiaka.market;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends Activity {

    View waitingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        waitingView = findViewById(R.id.progressBar);
        findViewById(R.id.btnConfig).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.launchConfigActivity(HomeActivity.this);
            }
        });

        findViewById(R.id.btn_enregistrement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.launchEnregistrementActivity(HomeActivity.this);
            }
        });

        findViewById(R.id.btn_envoyer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupNetwork(LocalDatabase.instance().getModel());
            }
        });
        findViewById(R.id.btnLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocationUtils.setupPermission(HomeActivity.this);

            }
        });
        findViewById(R.id.btn_paiement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PayRechercheActivity.class);
                HomeActivity.this.startActivity(intent);
            }
        });

    }


    private  void  setupNetwork(String content){
        if(content == null) {
            Toast.makeText(HomeActivity.this, "Aucune Donnée", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("xxxx",content);
        waitingView.setVisibility(View.VISIBLE);
        new Thread(() -> {

            boolean response = HttpHelper.postEntity(HttpHelper.REQUEST_POST,content);

            if (response) {
                runOnUiThread(() -> {
                    waitingView.setVisibility(View.GONE);
                    LocalDatabase.instance().clearLocaleTraffic();
                    Toast.makeText(HomeActivity.this, "Success ", Toast.LENGTH_SHORT).show();
                });
            } else {
                runOnUiThread(() -> {
                    waitingView.setVisibility(View.GONE);
                    Toast.makeText(HomeActivity.this, "Erreur ", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}
