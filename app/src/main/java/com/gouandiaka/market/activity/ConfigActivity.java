package com.gouandiaka.market.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.gouandiaka.market.HttpHelper;
import com.gouandiaka.market.utils.PrefUtils;
import com.gouandiaka.market.R;
import com.gouandiaka.market.utils.Utils;

public class ConfigActivity extends BaseActivity {

    Spinner spinnerVille,spinnerNature,spinnerPlace;

    EditText editTextCommune;
    private ProgressBar progressBar;
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
        progressBar = findViewById(R.id.progressBar);

        int placeNum = PrefUtils.getInt("place_num");
        spinnerPlace.setSelection(placeNum);

        int espaceNum = PrefUtils.getInt("espace_num");
        spinnerNature.setSelection(espaceNum);

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
                Toast.makeText(ConfigActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                finish();


            }
        });

       findViewById(R.id.btnLoca).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               HttpHelper.syncEntity(ConfigActivity.this,progressBar);

           }
       });

        findViewById(R.id.btn_local).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Utils.export(ConfigActivity.this);

            }
        });

    }



}
