package com.gouandiaka.market.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gouandiaka.market.R;
import com.gouandiaka.market.data.HttpHelper;
import com.gouandiaka.market.data.LocalDatabase;
import com.gouandiaka.market.entity.ApplicationConfig;
import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.utils.PrefUtils;
import com.gouandiaka.market.utils.RequestListener;
import com.gouandiaka.market.utils.Utils;
import com.gouandiaka.market.utils.Validator;
import com.gouandiaka.market.view.WaitingView;

import java.util.Calendar;
import java.util.Objects;

public class ConfigActivity extends BaseActivity implements RequestListener {

    private Spinner spinnerVille,spinnerNature,spinnerPlace, spinnerMoisPaiement, spinnerTickeType;

    private EditText anneePaiement;


    private WaitingView waitingView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        anneePaiement=findViewById(R.id.edittext_annee);
        spinnerVille = findViewById(R.id.spinnerville);
        spinnerMoisPaiement = findViewById(R.id.spinner_mois);
        spinnerNature = findViewById(R.id.spinnerProperty);
        spinnerPlace=findViewById(R.id.spinnerPlace);
        spinnerVille.setSelection(PrefUtils.getPrefPosition(spinnerVille,applicationConfig.getCity()));
        spinnerTickeType = findViewById(R.id.spinner_ticket_type_config);
        spinnerPlace.setSelection(PrefUtils.getPrefPosition(spinnerPlace,applicationConfig.getLocality()));
        anneePaiement.setText(String.valueOf(applicationConfig.getAnnee()));
        spinnerNature.setSelection(PrefUtils.getPrefPosition(spinnerNature,applicationConfig.getProperty()));
        spinnerMoisPaiement.setSelection(PrefUtils.getPrefPosition(spinnerMoisPaiement,applicationConfig.getMois()));
        spinnerTickeType.setSelection(PrefUtils.getPrefPosition(spinnerTickeType,applicationConfig.getTicketType()));
        waitingView = findViewById(R.id.waiting_view);
       findViewById(R.id.btnConfigSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ville = spinnerVille.getSelectedItem().toString();
                String place = spinnerPlace.getSelectedItem().toString();
                if(Utils.isSelectOrEmpty(ville)){
                    Toast.makeText(ConfigActivity.this,"Selectionner une ville",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Utils.isSelectOrEmpty(place)){
                    Toast.makeText(ConfigActivity.this,"Selectionner place",Toast.LENGTH_SHORT).show();
                    return;
                }
                String espace = spinnerNature.getSelectedItem().toString();
                if(Utils.isSelectOrEmpty(espace)){
                    Toast.makeText(ConfigActivity.this, "Selectionner un type", Toast.LENGTH_SHORT).show();
                    return;
                }


                String mois = spinnerMoisPaiement.getSelectedItem().toString();
                if(Utils.isSelectOrEmpty(mois)){
                    Toast.makeText(ConfigActivity.this, "Selectionner un mois paiement", Toast.LENGTH_SHORT).show();
                    return;
                }

                String ticket = spinnerTickeType.getSelectedItem().toString();
                if(Utils.isSelectOrEmpty(ticket)){
                    Toast.makeText(ConfigActivity.this, "Selectionner un  type ticket", Toast.LENGTH_SHORT).show();
                    return;
                }
                String annee = anneePaiement.getEditableText().toString();
                if(Utils.isEmpty(annee) || annee.length()!=4){
                    Toast.makeText(ConfigActivity.this, "Année incorrecte", Toast.LENGTH_SHORT).show();
                    return;
                }
                int year = Utils.convertToNumber(annee, Calendar.getInstance().get(Calendar.YEAR));
                if(year < 2025 || year > 2050){
                    Toast.makeText(ConfigActivity.this, "Année incorrecte", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Utils.isKalana(ville)){
                    place = ville;
                }
                applicationConfig.setAnnee(year);
                applicationConfig.setCity(ville);
                applicationConfig.setMois(mois);
                applicationConfig.setLocality(place);
                applicationConfig.setTicketType(ticket);
                applicationConfig.setProperty(espace);
                applicationConfig.setCommune(ApplicationConfig.COMMUNE);

                if(Validator.isValid(applicationConfig)){
                    LocalDatabase.instance().setConfig(applicationConfig);
                    waitingView.start(ConfigActivity.this,"Rechargement des données");
                    HttpHelper.reloadEntity(ConfigActivity.this,ConfigActivity.this);
                }else{
                    Toast.makeText(ConfigActivity.this, "Mauvaise configuration", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public void onSuccess(boolean success, Entity entity) {
        waitingView.stop(success,this,"Error chargement des données ");
        if(success){
            Toast.makeText(ConfigActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}
