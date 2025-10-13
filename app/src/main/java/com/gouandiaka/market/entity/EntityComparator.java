package com.gouandiaka.market.entity;

import android.location.Location;

import com.gouandiaka.market.utils.LocationUtils;
import com.gouandiaka.market.utils.Utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class EntityComparator implements Comparator<Entity> {

    public static final String SORT_STATUS = "STATUS";
    public static final String SORT_DISTANCE = "DISTANCE";

    private final String sortMode;
    private String location;

    public EntityComparator(String sortMode) {
        this.sortMode = sortMode;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int compare(Entity e1, Entity e2) {
        if (e1 == null || e2 == null) return 0;
        if (SORT_STATUS.equalsIgnoreCase(sortMode)) {
            if (e1.getLastPaiementDate() == null && e2.getLastPaiementDate() == null) return 0;
            if (e1.getLastPaiementDate() == null) return 1;
            if (e2.getLastPaiementDate() == null) return -1;
            return e2.getLastPaiementDate().compareTo(e1.getLastPaiementDate());
        }
        if (SORT_DISTANCE.equalsIgnoreCase(sortMode)) {
            Location myLocation = Utils.convertToLocation(location);
            double distanceE1 = LocationUtils.getDistanceLocation(e1, myLocation);
            double distanceE2 = LocationUtils.getDistanceLocation(e2, myLocation);
            return Double.compare(distanceE1, distanceE2);
        }

        return 0;
    }

    public static void sort(List<Entity> entities, String sortMode, String location) {
        if (!Utils.isNotEmptyList(entities)) return;

        EntityComparator comparator = new EntityComparator(sortMode);
        if (!Utils.isEmpty(location)) {
            comparator.setLocation(location);
        }

        Collections.sort(entities, comparator);
    }
}
