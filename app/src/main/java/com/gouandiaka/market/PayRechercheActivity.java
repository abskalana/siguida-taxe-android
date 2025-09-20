package com.gouandiaka.market;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;

import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.entity.EntityResponse;
import com.gouandiaka.market.entity.MyExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PayRechercheActivity extends BaseActivity implements LocationListener {

    private EditText editTextFilter;
    private ExpandableListView expandableListView;

    private EntityResponse entityResponse;
    List<String> listGroup;
    List<Entity> allEntities;
    HashMap<String, List<String>> listItem;
    MyExpandableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payrecherche);
        editTextFilter = findViewById(R.id.editTextFilter);
        expandableListView = findViewById(R.id.expandableListView);
        entityResponse = LocalDatabase.instance().getRemoteModel();
        allEntities = entityResponse.getEntities();
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();

        adapter = new MyExpandableListAdapter(this, listGroup, listItem);
        expandableListView.setAdapter(adapter);

        populateList(allEntities);

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
                List<Entity> filtered = new ArrayList<>();
                for (Entity e : allEntities) {
                    if (e.getContactNom().toLowerCase().contains(filter) ||
                            e.getContactPrenom().toLowerCase().contains(filter) ||
                            e.getContactPhone().toLowerCase().contains(filter)) {
                        filtered.add(e);
                    }
                }
                populateList(filtered);
            }
        });

    }

    void populateList(List<Entity> entities) {
        listGroup.clear();
        listItem.clear();

        for (Entity e : entities) {
            String groupName = e.getActivity();

            if (!listGroup.contains(groupName)) {
                listGroup.add(groupName);
                listItem.put(groupName, new ArrayList<>());
            }

            String childText = e.getContactNom() + " " + e.getContactPrenom() + " - " + e.getContactPhone();
            listItem.get(groupName).add(childText);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLocationChanged(@NonNull Location locations) {
        String place = PrefUtils.getString("place",null);
        if(Utils.isEmpty(place) ) return;
        populateList(entityResponse.filter(place, locations));
    }
}
