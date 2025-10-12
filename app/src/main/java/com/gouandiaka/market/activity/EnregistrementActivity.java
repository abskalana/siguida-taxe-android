package com.gouandiaka.market.activity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gouandiaka.market.R;
import com.gouandiaka.market.data.HttpHelper;
import com.gouandiaka.market.data.LocalDatabase;
import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.utils.LocationUtils;
import com.gouandiaka.market.utils.PrefUtils;
import com.gouandiaka.market.utils.RequestListener;
import com.gouandiaka.market.utils.Utils;
import com.gouandiaka.market.utils.Validator;
import com.gouandiaka.market.view.WaitingView;

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
        if (!Validator.isValid(model)) {
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

       findViewById(R.id.btnSave).setOnClickListener(view -> {
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

           if (Utils.isEmpty(coord)) {
               Toast.makeText(EnregistrementActivity.this, "Coordonnée GPS incorrecte", Toast.LENGTH_SHORT).show();
               return;
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
           if(Validator.isValid(model)){
               HttpHelper.sendEnRegistrement(model,EnregistrementActivity.this);
               waitingView.start(EnregistrementActivity.this);
           }else{
               Toast.makeText(view.getContext(), "Verifier les valeurs", Toast.LENGTH_SHORT).show();
           }

       });

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocationUtils.setupLocation(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        gpsView.setText(this.coord);
        gpsView.setTextColor(Color.GREEN);
    }

    @Override
    public void onSuccess(boolean b, Entity remoteEntity) {
        waitingView.stop(b,EnregistrementActivity.this);

        if(remoteEntity!=null){
            Utils.launchChoiceActivity(this,remoteEntity);
            finish();
        }else{
            if(Validator.isValid(model)) LocalDatabase.instance().addEntity(model);

        }
    }

}
