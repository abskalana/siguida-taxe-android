package com.gouandiaka.market;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.entity.Paiement;

public class PayConfirmActivity extends BaseActivity {

    private Entity  entity;

    private Paiement paiement;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiement);
        entity = new Gson().fromJson(getIntent().getStringExtra("entity"), Entity.class);
        //paiement.setEntityModel(entity.getId());
        ((TextView)findViewById(R.id.tv_nomcomplet)).setText(entity.getNomComplet());
        ((TextView)findViewById(R.id.tv_telephone)).setText(entity.getContactPhone());
        ((TextView)findViewById(R.id.tv_activity)).setText(entity.getActivity());
        ((TextView)findViewById(R.id.tv_nb_porte)).setText(String.valueOf(entity.getPorte()));
        ((TextView)findViewById(R.id.tv_property)).setText(String.valueOf(entity.getProperty()));
        ((TextView)findViewById(R.id.tv_montant)).setText(String.valueOf("500 FCFA"));
        findViewById(R.id.btn_process_paiement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = ((Spinner)findViewById(R.id.spinner_paiement)).getSelectedItem().toString();
                //paiement.setStatus(status);
                //paiement.setUser(PrefUtils.getInt("user_id"));
                LocalDatabase.instance().savePaiement(paiement);

            }
        });



    }

}
