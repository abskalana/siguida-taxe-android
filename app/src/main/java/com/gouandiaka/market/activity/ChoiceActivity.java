package com.gouandiaka.market.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gouandiaka.market.R;
import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.utils.LocationUtils;
import com.gouandiaka.market.utils.Utils;
import com.gouandiaka.market.utils.Validator;

public class ChoiceActivity extends BaseActivity  {


    private Entity remoteEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        remoteEntity = new Gson().fromJson(getIntent().getStringExtra("entity"), Entity.class);
        ((TextView)findViewById(R.id.label_success)).setText(remoteEntity.getNomComplet());
        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.launchEnregistrementActivity(ChoiceActivity.this);
                finish();
            }
        });
        findViewById(R.id.btn_continue_paiement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Validator.isValidRemote(remoteEntity)){
                    Utils.launchPayConfirmActivity(ChoiceActivity.this,remoteEntity);
                    finish();
                }else{
                    Toast.makeText(ChoiceActivity.this, "IMPOSSIBLE", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btn_accueil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocationUtils.setupLocation(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
