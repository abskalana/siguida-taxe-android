package com.gouandiaka.market.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.gouandiaka.market.HttpHelper;
import com.gouandiaka.market.utils.JsonSaver;
import com.gouandiaka.market.utils.PrefUtils;
import com.gouandiaka.market.R;
import com.gouandiaka.market.utils.RequestListener;
import com.gouandiaka.market.utils.Utils;

import java.util.Calendar;

public class ConfigActivity extends BaseActivity {

    Spinner spinnerVille,spinnerNature,spinnerPlace, spinnerMoisPaiement, spinnerTickeType;

    EditText editTextCommune, anneePaiement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        editTextCommune=findViewById(R.id.etconfigCommune);
        anneePaiement=findViewById(R.id.edittext_annee);
        spinnerVille = findViewById(R.id.spinnerville);
        spinnerMoisPaiement = findViewById(R.id.spinner_mois);
        spinnerNature = findViewById(R.id.spinnerProperty);
        spinnerPlace=findViewById(R.id.spinnerPlace);
        int villeNum = PrefUtils.getInt("ville_num");
        spinnerVille.setSelection(villeNum);

        spinnerTickeType = findViewById(R.id.spinner_ticket_type_config);

        int placeNum = PrefUtils.getInt("place_num");
        spinnerPlace.setSelection(placeNum);
        int defaultYear = Calendar.getInstance().get(Calendar.YEAR);
        anneePaiement.setText(PrefUtils.getString("annee",String.valueOf(defaultYear)));


        int espaceNum = PrefUtils.getInt("espace_num");
        spinnerNature.setSelection(espaceNum);



        int currentMois = Calendar.getInstance().get(Calendar.MONTH);
        int moiNum = PrefUtils.getInt("mois_num",currentMois+1);
        spinnerMoisPaiement.setSelection(moiNum);

        int typeNum = PrefUtils.getInt("ticket_num");
        spinnerTickeType.setSelection(typeNum);

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
                int year = Utils.convertToNumber(annee, defaultYear);
                if(year < 2025 || year > 2050){
                    Toast.makeText(ConfigActivity.this, "Année incorrecte", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Utils.isKalana(ville)){
                    place = ville;
                }
                PrefUtils.save("ville",ville);
                PrefUtils.save("mois",mois);
                PrefUtils.save("annee",annee);
                PrefUtils.save("place", place);
                PrefUtils.save("espace", espace);
                PrefUtils.save("ticket", ticket);
                PrefUtils.setInt("espace_num", spinnerNature.getSelectedItemPosition());
                PrefUtils.setInt("ville_num", spinnerVille.getSelectedItemPosition());
                PrefUtils.setInt("place_num", spinnerPlace.getSelectedItemPosition());
                PrefUtils.setInt("mois_num", spinnerMoisPaiement.getSelectedItemPosition());
                PrefUtils.setInt("ticket_num", spinnerTickeType.getSelectedItemPosition());

                PrefUtils.save("commune", "150202");
                Toast.makeText(ConfigActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

       findViewById(R.id.btnbackup).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               JsonSaver.dumpFile(ConfigActivity.this);
           }
       });
    }

}
