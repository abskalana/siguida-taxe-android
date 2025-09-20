package com.gouandiaka.market;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ConfigActivity extends BaseActivity {

    Spinner spinnerVille,spinnerNature,spinnerPlace;

    EditText editTextCommune;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        editTextCommune=findViewById(R.id.etconfigCommune);
        spinnerVille = findViewById(R.id.spinnerville);
        spinnerNature = findViewById(R.id.spinnerProperty);
        spinnerPlace=findViewById(R.id.spinnerPlace);
        int villeNum = PrefUtils.getInt("ville_num");
        spinnerVille.setSelection(villeNum);

        int placeNum = PrefUtils.getInt("place_num");
        spinnerPlace.setSelection(placeNum);

        int espaceNum = PrefUtils.getInt("espace_num");
        spinnerNature.setSelection(espaceNum);

       findViewById(R.id.btnConfigSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ville = spinnerVille.getSelectedItem().toString();
                String place = spinnerPlace.getSelectedItem().toString();
                if(ville.startsWith("Selec")){
                    Toast.makeText(ConfigActivity.this,"Selectionner une ville",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(place.startsWith("Selec")){
                    Toast.makeText(ConfigActivity.this,"Selectionner place",Toast.LENGTH_SHORT).show();
                    return;
                }
                String espace = spinnerNature.getSelectedItem().toString();
                if(espace.startsWith("Selec")){
                    Toast.makeText(ConfigActivity.this, "Selectionner un type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Utils.isKalana(ville)){
                    place = ville;
                }
                PrefUtils.save("ville",ville);
                PrefUtils.save("place", place);
                PrefUtils.save("espace", espace);
                PrefUtils.setInt("espace_num", spinnerNature.getSelectedItemPosition());
                PrefUtils.setInt("ville_num", spinnerVille.getSelectedItemPosition());
                PrefUtils.setInt("place_num", spinnerPlace.getSelectedItemPosition());
                PrefUtils.save("commune", "150202");
                Toast.makeText(ConfigActivity.this, "Success", Toast.LENGTH_SHORT).show();
                finish();


            }
        });

    }



}
