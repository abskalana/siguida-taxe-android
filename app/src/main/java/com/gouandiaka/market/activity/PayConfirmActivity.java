package com.gouandiaka.market.activity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gouandiaka.market.R;
import com.gouandiaka.market.data.HttpHelper;
import com.gouandiaka.market.data.LocalDatabase;
import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.entity.Paiement;
import com.gouandiaka.market.utils.LocationUtils;
import com.gouandiaka.market.utils.PrefUtils;
import com.gouandiaka.market.utils.RequestListener;
import com.gouandiaka.market.utils.Utils;
import com.gouandiaka.market.utils.Validator;
import com.gouandiaka.market.view.WaitingView;

public class PayConfirmActivity extends BaseActivity implements RequestListener {

    Entity entity;
    private Spinner spinnerMois,spinnerTicket;
    private EditText editTextMontant, editTextCmt;

    private Paiement paiement;

    private Spinner spinner;
    private TextView phone1, paiementStatus;



    private WaitingView waitingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiement);
        entity = new Gson().fromJson(getIntent().getStringExtra("entity"), Entity.class);
        spinner = findViewById(R.id.spinner_paiement);
        editTextCmt = findViewById(R.id.tv_comment);
        phone1 = findViewById(R.id.tv_telephone1);
        paiementStatus = findViewById(R.id.tv_paiement_status);


        editTextMontant = findViewById(R.id.tv_montant);
        spinnerMois = findViewById(R.id.spinner_ticket_mois);
        spinnerMois.setSelection(PrefUtils.getPrefPosition(spinnerMois,applicationConfig.getMois()));
        spinnerTicket = findViewById(R.id.spinner_ticket_type);
        spinnerTicket.setSelection(PrefUtils.getPrefPosition(spinnerTicket,applicationConfig.getTicketType()));
        waitingView = findViewById(R.id.waiting_view);

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
        findViewById(R.id.menu_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    LocationUtils.launchGooglemap(PayConfirmActivity.this,entity.getCoord(),entity.getNomComplet());
                } catch (Exception e) {
                    Toast.makeText(PayConfirmActivity.this,"Impossible",Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.menu_menu_direction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    LocationUtils.launchNavigation(PayConfirmActivity.this,entity.getCoord());
                } catch (Exception e) {
                    Toast.makeText(PayConfirmActivity.this,"Impossible",Toast.LENGTH_SHORT).show();
                }

            }
        });



        if(entity.getPaiement()!=null){
            this.paiement = entity.getPaiement();
            editTextCmt.setText(this.paiement.getCommentaire());
            editTextMontant.setText(String.valueOf(entity.getPaiement().getValue()));
            spinner.setSelection(((ArrayAdapter)spinner.getAdapter()).getPosition(entity.getPaiementStatus()));
            if(entity.is_paie()){
                editTextMontant.setEnabled(false);
                spinner.setEnabled(false);
                spinnerTicket.setEnabled(false);
                spinnerMois.setEnabled(false);
                editTextCmt.setEnabled(false);
                ((TextView)findViewById(R.id.tv_time)).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.tv_time)).setText(paiement.getDate());
                findViewById(R.id.btn_process_paiement).setVisibility(View.GONE);
            }

        }else{
            this.paiement =  applicationConfig.getPaiement(entity.getId());;
        }
        paiementStatus.setText(Utils.getColoredStatus(entity.getPaiementStatus()));

        ((TextView) findViewById(R.id.tv_info)).setText(entity.getInfo());
        findViewById(R.id.btn_process_paiement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = spinner.getSelectedItem().toString();
                if (Utils.isSelectOrEmpty(status)) {
                    Toast.makeText(view.getContext(), "Selectionner un status paiement", Toast.LENGTH_SHORT).show();
                    return;
                }
                String ticketType =spinnerTicket.getSelectedItem().toString();
                if (Utils.isSelectOrEmpty(ticketType)) {
                    Toast.makeText(view.getContext(), "Selectionner un type ticket ", Toast.LENGTH_SHORT).show();
                    return;
                }

                String ticketMois = spinnerMois.getSelectedItem().toString();
                if (Utils.isSelectOrEmpty(ticketMois)) {
                    Toast.makeText(view.getContext(), "Selectionner le mois ", Toast.LENGTH_SHORT).show();
                    return;
                }

                String montant = editTextMontant.getEditableText().toString();
                int value = Utils.convertToNumber(montant,0 );
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
                if (Validator.isValid(paiement)) {
                    waitingView.start(PayConfirmActivity.this);
                    HttpHelper.sendPaiement(paiement, PayConfirmActivity.this);
                } else {
                    Toast.makeText(view.getContext(), "Verifier les valeurs", Toast.LENGTH_SHORT).show();
                }

            }
        });

        findViewById(R.id.btn_paiement_retour).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);


    }

    @Override
    public void onSuccess(boolean b, Entity remoteEntity) {
        waitingView.stop(b,this);
        if(Validator.isValid(remoteEntity)){
            extracted(remoteEntity);
            Toast.makeText(this,"paiement envoyé avec success", Toast.LENGTH_SHORT).show();
        }else{
            if(Validator.isValid(paiement))LocalDatabase.instance().savePaiement(paiement);
        }
    }

    private void extracted(Entity entity) {
        if(entity.is_paie()){
            findViewById(R.id.btn_process_paiement).setVisibility(View.GONE);
            editTextMontant.setText(String.valueOf(entity.getPaiement().getValue()));
            spinner.setSelection(PrefUtils.getPrefPosition(spinner,entity.getPaiementStatus()));
            editTextMontant.setEnabled(false);
            spinner.setEnabled(false);
            spinnerTicket.setEnabled(false);
            spinnerMois.setEnabled(false);
            editTextCmt.setEnabled(false);
            findViewById(R.id.btn_paiement_retour).setVisibility(View.VISIBLE);
            paiementStatus.setText(Utils.getColoredStatus(entity.getPaiementStatus()));
        }
    }
}
