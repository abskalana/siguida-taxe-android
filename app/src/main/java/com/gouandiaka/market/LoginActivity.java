package com.gouandiaka.market;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginActivity extends Activity {


    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText passworeditT = findViewById(R.id.etPasswor);
        EditText editText = findViewById(R.id.etidentifiant);
        PrefUtils.init(this);
        LocalDatabase.init(this);
        if(!shoulRequest()){
            Utils.launchAccueilActivity(LoginActivity.this);
            return;
        }

        progressBar = findViewById(R.id.progressBar);
        String saveUser = PrefUtils.getString("user");
        if(!Utils.isEmpty(saveUser)){
            editText.setText(saveUser);
        }
        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = editText.getEditableText().toString().trim();
                String monMotDePasse = passworeditT.getEditableText().toString().trim();
                if(Utils.isEmpty(userName) || Utils.isEmpty(monMotDePasse)) return;

                progressBar.setVisibility(View.VISIBLE);

                new Thread(() -> {

                    int response = HttpHelper.login(HttpHelper.LOGIN_URL, userName, monMotDePasse);

                    if (response >-1) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            PrefUtils.save("user",userName.trim());
                            PrefUtils.setInt("user_id",response);
                            PrefUtils.save("time",System.currentTimeMillis());
                            Utils.launchAccueilActivity(LoginActivity.this);

                        });
                    } else {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Erreur connexion", Toast.LENGTH_SHORT).show();
                        });
                    }
                }).start();
            }
        });

    }


    private boolean shoulRequest(){
        long time =  System.currentTimeMillis() -PrefUtils.getLong("time");
        return time > 1000*60*60*23;
    }
}
