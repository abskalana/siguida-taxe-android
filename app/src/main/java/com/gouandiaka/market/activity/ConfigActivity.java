package com.gouandiaka.market.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gouandiaka.market.R;
import com.gouandiaka.market.utils.PrefUtils;
import com.gouandiaka.market.utils.Utils;

import java.util.Calendar;

public class ConfigActivity extends BaseActivity {

    private Spinner spinnerVille,spinnerNature,spinnerPlace, spinnerMoisPaiement, spinnerTickeType;

    private EditText anneePaiement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        anneePaiement=findViewById(R.id.edittext_annee);
        spinnerVille = findViewById(R.id.spinnerville);
        spinnerMoisPaiement = findViewById(R.id.spinner_mois);
        spinnerNature = findViewById(R.id.spinnerProperty);
        spinnerPlace=findViewById(R.id.spinnerPlace);
        spinnerVille.setSelection(PrefUtils.getPrefPosition(spinnerVille,"ville"));
        spinnerTickeType = findViewById(R.id.spinner_ticket_type_config);
        spinnerPlace.setSelection(PrefUtils.getPrefPosition(spinnerPlace,"place"));
        anneePaiement.setText(String.valueOf(PrefUtils.getAnnee()));
        spinnerNature.setSelection(PrefUtils.getPrefPosition(spinnerNature,"espace"));
        int currentMois = Calendar.getInstance().get(Calendar.MONTH);
        int moiNum = PrefUtils.getPrefPosition(spinnerMoisPaiement,"mois");
        if(moiNum == 0) moiNum = currentMois+1;
        spinnerMoisPaiement.setSelection(moiNum);
        spinnerTickeType.setSelection(PrefUtils.getPrefPosition(spinnerTickeType,"ticket"));

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

                if("ESPACE PUBLIC".equalsIgnoreCase(espace)){
                    place = "Kalana";
                }

                String mois = spinnerMoisPaiement.getSelectedItem().toString();
                if(Utils.isSelectOrEmpty(mois)){
                    Toast.makeText(ConfigActivity.this, "Selectionner un mois paiement", Toast.LENGTH_SHORT).show();
                    return;
                }

                String ticket = spinnerTickeType.getSelectedItem().toString();
                if(Utils.isSelectOrEmpty(ticket)){
                    Toast.makeText(ConfigActivity.this, "Selectionner un mois type ticket", Toast.LENGTH_SHORT).show();
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
                PrefUtils.save("ville",ville);
                PrefUtils.save("mois",mois);
                PrefUtils.setInt("annee",year);
                PrefUtils.save("place", place);
                PrefUtils.save("espace", espace);
                PrefUtils.save("ticket", ticket);

                PrefUtils.save("commune", "150202");
                Toast.makeText(ConfigActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }

}
