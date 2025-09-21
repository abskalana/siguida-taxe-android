package com.gouandiaka.market.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gouandiaka.market.LocalDatabase;
import com.gouandiaka.market.R;
import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.entity.Paiement;
import com.gouandiaka.market.utils.PrefUtils;
import com.gouandiaka.market.utils.Utils;

public class PayConfirmActivity extends BaseActivity {

    private Entity  entity;

    private EditText EdTicketNum, editTextMontant, editTextCmt;

    private Paiement paiement;
    private  TextView phone1, phone2;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiement);
        entity = new Gson().fromJson(getIntent().getStringExtra("entity"), Entity.class);
        editTextCmt = findViewById(R.id.tv_comment);
        phone1 = ((TextView) findViewById(R.id.tv_telephone1));
        phone2 = ((TextView) findViewById(R.id.tv_telephone2));
        editTextMontant = findViewById(R.id.tv_montant);
        EdTicketNum = findViewById(R.id.tv_ticket_num);
        paiement = new Paiement(PrefUtils.getInt("user_id"),entity.getId());
        ((TextView)findViewById(R.id.tv_nomcomplet)).setText(entity.getNomComplet());
         if (entity.getTelephone1() != null) {
             phone1.setVisibility(View.VISIBLE);
             phone1.setText("Tel : "+entity.getTelephone1());
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
             phone2.setText("Tel : "+entity.getTelephone2());
             phone2.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                      Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + entity.getTelephone2()));
                     view.getContext().startActivity(intent);
                 }
             });

         }
        ((TextView)findViewById(R.id.tv_info)).setText(entity.getInfo());
        findViewById(R.id.btn_process_paiement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = ((Spinner)findViewById(R.id.spinner_paiement)).getSelectedItem().toString();
                if(Utils.isSelectOrEmpty(status)){
                    Toast.makeText(view.getContext(), "Selectionner un status paiement",Toast.LENGTH_SHORT).show();
                    return;
                }
                String ticketType = ((Spinner)findViewById(R.id.spinner_ticket_type)).getSelectedItem().toString();
                if(Utils.isSelectOrEmpty(ticketType)){
                    Toast.makeText(view.getContext(), "Selectionner un type ticket ",Toast.LENGTH_SHORT).show();
                    return;
                }
                String ticketNum = EdTicketNum.getEditableText().toString();
                int num = Utils.convertToNumber(ticketNum, -1);
                if(num <1){
                    Toast.makeText(view.getContext(), "Ticket num incorrect",Toast.LENGTH_SHORT).show();
                    return;
                }
                paiement.setTicketNum(ticketNum);
                String montant = editTextMontant.getEditableText().toString();
                int value = Utils.convertToNumber(montant, -1);
                if(num < 100){
                    Toast.makeText(view.getContext(), "montant incorrect",Toast.LENGTH_SHORT).show();
                    return;
                }
                paiement.setValue(value);
                paiement.setStatus(status);
                paiement.setCoord(coord);
                paiement.setCommentaire(editTextCmt.getEditableText().toString());
                paiement.setTicketType(ticketType);
                if(Paiement.isValid(paiement)){
                    LocalDatabase.instance().savePaiement(paiement);
                    Toast.makeText(view.getContext(), "SUCCESS",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(view.getContext(), "Verifier les valeurs",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

}
