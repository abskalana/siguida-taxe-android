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
import android.widget.Toast;

import com.gouandiaka.market.R;
import com.gouandiaka.market.data.HttpHelper;
import com.gouandiaka.market.data.LocalDatabase;
import com.gouandiaka.market.entity.ApplicationConfig;
import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.utils.LocationUtils;
import com.gouandiaka.market.utils.PrefUtils;
import com.gouandiaka.market.utils.RequestListener;
import com.gouandiaka.market.utils.Utils;
import com.gouandiaka.market.view.MyExpandableListAdapter;
import com.gouandiaka.market.view.WaitingView;

import java.util.ArrayList;
import java.util.List;

public class PayRechercheActivity extends BaseActivity implements LocationListener, RequestListener {

    private EditText editTextFilter;
    private ExpandableListView expandableListView;
    private WaitingView waitingView;


    List<Entity> allEntities;
    private Button charger;

    private Location location;


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

        String title = "Paiement "+ applicationConfig.getLocality() +" - "+applicationConfig.getMois() + " "+ applicationConfig.getAnnee();
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

        findViewById(R.id.btnProche).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(location == null) location = Utils.convertToLocation(coord);
                if(location == null){
                    Toast.makeText(PayRechercheActivity.this, "Position non precise, activer le GPS", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Entity> entitiesFilter = LocationUtils.filterByLocation(allEntities,Utils.convertToString(location),100);
                if(Utils.isNotEmptyList(entitiesFilter)){
                    type =  MyExpandableListAdapter.TYPE_LOCATION;
                    adapter = new MyExpandableListAdapter(entitiesFilter, MyExpandableListAdapter.TYPE_LOCATION);
                    expandableListView.setAdapter(adapter);
                    show();
                }else{
                    Toast.makeText(PayRechercheActivity.this, "Position non precise, activer le GPS ou trop loin", Toast.LENGTH_SHORT).show();
                }
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
    public void onLocationChanged(Location locations) {
        super.onLocationChanged(locations);
        this.location = locations;

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
        setupAdapter(type);
    }
    private void setupAdapter(String type){
        allEntities = LocalDatabase.instance().getRemoteModel();
        adapter = new MyExpandableListAdapter(allEntities, type);
        expandableListView.setAdapter(adapter);
        charger.setText("Charger (" + allEntities.size() + " )");
    }

    @Override
    public void onSuccess(boolean b,Entity entity) {
        waitingView.stop(b,this);
        if(b){
            setupAdapter(type);
        }
    }
}
