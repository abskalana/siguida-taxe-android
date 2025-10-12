package com.gouandiaka.market.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Localdata {

    private List<Entity> entities = new ArrayList<>();
    private List<Paiement> paiements = new ArrayList<>();

    public Localdata(List<Entity> entities, List<Paiement> paiements) {
        this.entities = new ArrayList<>(new HashSet<>(entities));
        this.paiements = new ArrayList<>(new HashSet<>(paiements));
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public List<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(List<Paiement> paiements) {
        this.paiements = paiements;
    }
}
