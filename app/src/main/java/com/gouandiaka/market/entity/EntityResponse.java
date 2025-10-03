package com.gouandiaka.market.entity;

import android.location.Location;

import com.gouandiaka.market.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EntityResponse {

    private List<Entity> entities = new ArrayList<>();

    public EntityResponse(List<Entity> entities) {
        this.entities = entities;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Entity> filter(String locality, Location myLocation) {
        List<Entity> entities = new ArrayList<>();
        for (Entity entity : this.entities) {
            if (Utils.isEmpty(entity.getContactPhone()) || Utils.isEmpty(entity.getCoord())) {
                continue;
            }
            if (entity.getLocality().equalsIgnoreCase(locality)) {
                entities.add(entity);
            }
            Location location = Utils.convertToLocation(entity.getCoord());

            if (location != null && myLocation != null && myLocation.distanceTo(location) < 300) {
                entities.add(entity);
            }
        }
        return entities;
    }
}
