package com.gouandiaka.market.activity;

import static android.view.View.VISIBLE;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.gouandiaka.market.HttpHelper;
import com.gouandiaka.market.LocalDatabase;
import com.gouandiaka.market.R;
import com.gouandiaka.market.WaitingView;
import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.utils.LocationUtils;
import com.gouandiaka.market.utils.PrefUtils;
import com.gouandiaka.market.utils.RequestListener;
import com.gouandiaka.market.utils.Utils;

import java.util.List;

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
                if(Entity.isValidEntity(remoteEntity)){
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
