package com.gouandiaka.market.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gouandiaka.market.HttpHelper;
import com.gouandiaka.market.LocalDatabase;
import com.gouandiaka.market.utils.LocationUtils;
import com.gouandiaka.market.utils.PrefUtils;
import com.gouandiaka.market.R;
import com.gouandiaka.market.utils.Utils;

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
            Utils.launchAccueilActivity(LoginActivity.this,false);
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
                            Utils.launchAccueilActivity(LoginActivity.this,true);

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean shoulRequest(){
        long time =  System.currentTimeMillis() -PrefUtils.getLong("time");
        return time > 1000*60*60*8;
    }
}
