package com.gouandiaka.market.entity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {


    private List<String> listGroup;
    private HashMap<String, List<Entity>> listItem;

    public static final String TYPE_ACTIVITY= "activity";
    public static final String TYPE_STATUS= "status";
    public static final String TYPE_NOM= "nom";

    public MyExpandableListAdapter(List<Entity> entities, String type) {
        this.listItem = new HashMap<>();
        this.listGroup = new ArrayList<>();
        for (Entity e : entities) {

            String groupName = e.getActivity();
            if(TYPE_NOM.equalsIgnoreCase(type)) groupName = e.getContactNom();
            if(TYPE_STATUS.equalsIgnoreCase(type)) groupName = e.getPaiementStatus();

            if (!listGroup.contains(groupName)) {
                listGroup.add(groupName);
                listItem.put(groupName, new ArrayList<>());
            }
            listItem.get(groupName).add(e);
        }
        Collections.sort(listGroup);
    }



    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listItem.get(listGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listItem.get(listGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    // Vue pour le groupe
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String groupTitle = (String) getGroup(groupPosition);
        if (convertView instanceof GroupingItemView) {
            GroupingItemView placeItemView = (GroupingItemView) convertView;
            placeItemView.reuse(groupTitle);
            return placeItemView;
        }
        return new GroupingItemView(parent.getContext(), groupTitle);

    }

    // Vue pour l'enfant
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        String title = listGroup.get(groupPosition);

        Entity localePlace = Objects.requireNonNull(listItem.get(title)).get(childPosition);
        if (convertView instanceof PlacesItemView) {
            PlacesItemView placesItemView = (PlacesItemView) convertView;
            placesItemView.reuse(localePlace.getContactNom(), localePlace.getContactPrenom(), localePlace.getContactPhone(),localePlace.getPaiementStatus());
            return placesItemView;
        }
        return new PlacesItemView(parent.getContext(), localePlace.getContactNom(), localePlace.getContactPrenom(), localePlace.getContactPhone(),localePlace.getPaiementStatus());

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
