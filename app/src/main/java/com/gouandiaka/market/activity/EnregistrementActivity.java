package com.gouandiaka.market.activity;

import static android.view.View.VISIBLE;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

public class EnregistrementActivity extends BaseActivity implements RequestListener {


    private Spinner spinnerCategories, spinnerType, spinnerEtat;

    private TextView gpsView;

    private WaitingView waitingView;
    private  Entity model;



    private EditText editTextNom, editTextPreNom, editTexttelephone,  editTextnbrPorte,editTextNumero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enregistrement);
        model = PrefUtils.getEntity();
        if (model.isInCorrect()) {
            Utils.launchConfigActivity(this);
            return;
        }

        gpsView = findViewById(R.id.gps_view);
        gpsView.setTextColor(Color.RED);
        waitingView = findViewById(R.id.waiting_view);
        spinnerCategories = findViewById(R.id.spinnerCategories);
        spinnerType = findViewById(R.id.spinnerTypeProperty);
        spinnerEtat = findViewById(R.id.spinnerEtat);
        editTextNom = findViewById(R.id.etNom);
        editTextPreNom = findViewById(R.id.etPrenom);
        editTexttelephone = findViewById(R.id.ettelephone);
        editTextnbrPorte = findViewById(R.id.etnbrPorte);
        editTextNumero = findViewById(R.id.etnbrBoutique);

       findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Entity model = PrefUtils.getEntity();
                int numero = 1;
                if (Utils.isEmpty(editTextNom.getEditableText().toString())) {
                    editTextNom.setError("Nom incorrecte");
                    Toast.makeText(EnregistrementActivity.this, "Nom incorrecte", Toast.LENGTH_SHORT).show();
                    return;
                }
                String num = editTextNumero.getEditableText().toString();
                numero = Utils.convertToNumber(num,numero);


                model.setContactName(editTextNom.getEditableText().toString());

                if (Utils.isEmpty(editTextPreNom.getEditableText().toString())) {
                    Toast.makeText(EnregistrementActivity.this, "Prenom incorrecte", Toast.LENGTH_SHORT).show();
                    editTextPreNom.setError("Prenom incorrecte");
                    return;
                }
                model.setContactPrenom(editTextPreNom.getEditableText().toString());

                if (!Utils.isValidPhone(editTexttelephone.getEditableText().toString())) {
                    editTexttelephone.setError("Telephone incorrecte");
                    Toast.makeText(EnregistrementActivity.this, "Telephone incorrecte", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (coord == null) {
                    Toast.makeText(EnregistrementActivity.this, "Coordonnée GPS incorrecte", Toast.LENGTH_SHORT).show();
                }
                String activity =  spinnerCategories.getSelectedItem().toString();
                if(Utils.isSelectOrEmpty(activity)){
                    Toast.makeText(EnregistrementActivity.this, "Categorie incorrecte", Toast.LENGTH_SHORT).show();
                    return;
                }
                model.setNumero(numero);
                model.setContactPhone(editTexttelephone.getEditableText().toString());
                model.setPorte(Utils.convertToNumber(editTextnbrPorte.getEditableText().toString(), 1));
                model.setActivity(activity);
                model.setTypeProperty(spinnerType.getSelectedItem().toString());
                model.setStatus(spinnerEtat.getSelectedItem().toString());
                model.setCoord(coord);
                HttpHelper.sendEnRegistrement(model,EnregistrementActivity.this);
                waitingView.start(EnregistrementActivity.this);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocationUtils.setupLocation(this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        super.onLocationChanged(location);
        gpsView.setText(this.coord);
        gpsView.setTextColor(Color.GREEN);
    }

    @Override
    public void onSuccess(boolean b, List<Entity> entityList) {
        waitingView.stop(b,EnregistrementActivity.this);
        Log.i("xxxx", entityList.size()+ "");
        if(Utils.isNotEmptyList(entityList)){
            Entity remoteEntity = entityList.get(0);
            Log.i("xxxx", entityList.size()+ ""+remoteEntity.toString());
            Utils.launchChoiceActivity(this,remoteEntity);
            finish();
        }else{
            if(model!=null)LocalDatabase.instance().addEntity(model);

        }
    }

}
