package com.gouandiaka.market.activity;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gouandiaka.market.HttpHelper;
import com.gouandiaka.market.LocalDatabase;
import com.gouandiaka.market.R;
import com.gouandiaka.market.WaitingView;
import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.entity.MyExpandableListAdapter;
import com.gouandiaka.market.utils.PrefUtils;
import com.gouandiaka.market.utils.RequestListener;
import com.gouandiaka.market.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PayRechercheActivity extends BaseActivity implements LocationListener, RequestListener {

    private EditText editTextFilter;
    private ExpandableListView expandableListView;
    private WaitingView waitingView;

    //private EntityResponse entityResponse;

    List<Entity> allEntities;
    private Button charger;

    MyExpandableListAdapter adapter;

    private String type = MyExpandableListAdapter.TYPE_ACTIVITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payrecherche);
        editTextFilter = findViewById(R.id.editTextFilter);
        expandableListView = findViewById(R.id.expandableListView);
        waitingView = findViewById(R.id.waiting_view);
        charger = findViewById(R.id.btn_recharger);

        String locality = PrefUtils.getString("place");
        String title = "Paiement "+ locality +" - "+PrefUtils.getString("mois") + " "+ PrefUtils.getString("annee");
        ((TextView)findViewById(R.id.title_paiement)).setText(title);

        findViewById(R.id.btnParActivite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new MyExpandableListAdapter(allEntities, MyExpandableListAdapter.TYPE_ACTIVITY);
                expandableListView.setAdapter(adapter);
                type =  MyExpandableListAdapter.TYPE_ACTIVITY;
            }
        });

        findViewById(R.id.btnParNom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new MyExpandableListAdapter(allEntities, MyExpandableListAdapter.TYPE_NOM);
                expandableListView.setAdapter(adapter);
                type =  MyExpandableListAdapter.TYPE_NOM;

            }
        });

        findViewById(R.id.btnParStatus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new MyExpandableListAdapter(allEntities, MyExpandableListAdapter.TYPE_STATUS);
                expandableListView.setAdapter(adapter);
                type =  MyExpandableListAdapter.TYPE_STATUS;
            }
        });

        charger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waitingView.start(PayRechercheActivity.this);
                HttpHelper.reloadEntity(PayRechercheActivity.this,PayRechercheActivity.this);
            }
        });



        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Entity localePlace = (Entity) adapter.getChild(groupPosition, childPosition);
                Utils.launchPayConfirmActivity(PayRechercheActivity.this, localePlace);
                return false;
            }
        });

// Filtrage dynamique
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String filter = s.toString().toLowerCase();
                if (Utils.isEmpty(filter)) return;
                List<Entity> filtered = new ArrayList<>();
                for (Entity e : allEntities) {
                    if (e.getContactNom().toLowerCase().contains(filter) ||
                            e.getContactPrenom().toLowerCase().contains(filter) ||
                            e.getContactPhone().toLowerCase().contains(filter)) {
                        filtered.add(e);
                    }
                }
                adapter = new MyExpandableListAdapter(filtered, type);
                expandableListView.setAdapter(adapter);
                show();


            }
        });

    }

    @Override
    public void onLocationChanged(@NonNull Location locations) {
        String place = PrefUtils.getString("place", null);
        if (Utils.isEmpty(place)) return;

    }

    private void show(){
        int groupCount = adapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            expandableListView.expandGroup(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupAdapter();
    }
    private void setupAdapter(){
        allEntities = LocalDatabase.instance().getRemoteModel();
        adapter = new MyExpandableListAdapter(allEntities, MyExpandableListAdapter.TYPE_ACTIVITY);
        expandableListView.setAdapter(adapter);
        charger.setText("Charger (" + allEntities.size() + " )");
    }

    @Override
    public void onSuccess(boolean b,List<Entity> allEntities) {
        waitingView.stop(b,this);
        if(b){
            setupAdapter();
        }
    }
}
