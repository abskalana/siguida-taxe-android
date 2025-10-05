package com.gouandiaka.market.activity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
import com.gouandiaka.market.entity.Paiement;
import com.gouandiaka.market.utils.PrefUtils;
import com.gouandiaka.market.utils.RequestListener;
import com.gouandiaka.market.utils.Utils;

public class PayConfirmActivity extends BaseActivity implements RequestListener {

    Entity entity;

    private EditText editTextMontant, editTextCmt;

    private Paiement paiement;
    private TextView phone1, phone2;

    private TextView gpsView;

    private WaitingView waitingView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiement);
        entity = new Gson().fromJson(getIntent().getStringExtra("entity"), Entity.class);
        editTextCmt = findViewById(R.id.tv_comment);
        phone1 = ((TextView) findViewById(R.id.tv_telephone1));
        phone2 = ((TextView) findViewById(R.id.tv_telephone2));
        gpsView = findViewById(R.id.gps_view);
        gpsView.setTextColor(Color.RED);
        editTextMontant = findViewById(R.id.tv_montant);
        waitingView = findViewById(R.id.waiting_view);
        paiement = new Paiement(PrefUtils.getInt("user_id"), entity.getId());
        ((TextView) findViewById(R.id.tv_nomcomplet)).setText(entity.getNomComplet());
        if (entity.getTelephone1() != null) {
            phone1.setVisibility(View.VISIBLE);
            phone1.setText("Tel : " + entity.getTelephone1());
            phone1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + entity.getTelephone1()));
                    view.getContext().startActivity(intent);
                }
            });
        }
        if (entity.getTelephone2() != null) {
            phone2.setVisibility(View.VISIBLE);
            phone2.setText("Tel : " + entity.getTelephone2());
            phone2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + entity.getTelephone2()));
                    view.getContext().startActivity(intent);
                }
            });

        }
        ((TextView) findViewById(R.id.tv_info)).setText(entity.getInfo());
        findViewById(R.id.btn_process_paiement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = ((Spinner) findViewById(R.id.spinner_paiement)).getSelectedItem().toString();
                if (Utils.isSelectOrEmpty(status)) {
                    Toast.makeText(view.getContext(), "Selectionner un status paiement", Toast.LENGTH_SHORT).show();
                    return;
                }
                String ticketType = ((Spinner) findViewById(R.id.spinner_ticket_type)).getSelectedItem().toString();
                if (Utils.isSelectOrEmpty(ticketType)) {
                    Toast.makeText(view.getContext(), "Selectionner un type ticket ", Toast.LENGTH_SHORT).show();
                    return;
                }

                String ticketMois = ((Spinner) findViewById(R.id.spinner_ticket_mois)).getSelectedItem().toString();
                if (Utils.isSelectOrEmpty(ticketMois)) {
                    Toast.makeText(view.getContext(), "Selectionner le mois ", Toast.LENGTH_SHORT).show();
                    return;
                }

                String montant = editTextMontant.getEditableText().toString();
                int value = Utils.convertToNumber(montant, -1);
                if (value < 100) {
                    Toast.makeText(view.getContext(), "montant incorrect", Toast.LENGTH_SHORT).show();
                    return;
                }
                paiement.setMois(ticketMois);
                paiement.setValue(value);
                paiement.setStatus(status);
                paiement.setCoord(coord);
                paiement.setCommentaire(editTextCmt.getEditableText().toString());
                paiement.setTicketType(ticketType);
                if (Paiement.isValid(paiement)) {
                    waitingView.start();
                    HttpHelper.sendPaiement(paiement, PayConfirmActivity.this);
                } else {
                    Toast.makeText(view.getContext(), "Verifier les valeurs", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        super.onLocationChanged(location);
        gpsView.setText(this.coord);
        gpsView.setTextColor(Color.GREEN);

    }

    @Override
    public void onSuccess(boolean b) {
        waitingView.stop(b);
        if(b){
            Toast.makeText(this,"paiement Envoyé avec success", Toast.LENGTH_SHORT).show();
        }else{
            if(paiement!=null)LocalDatabase.instance().savePaiement(paiement);
        }
    }
}
