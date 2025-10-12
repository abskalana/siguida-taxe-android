package com.gouandiaka.market.entity;

import com.google.gson.annotations.SerializedName;

public class Paiement {


    private String id;
    @SerializedName("value")
    private int value;

    @SerializedName("mois")
    private String mois;

    @SerializedName("period")
    private String period;

    @SerializedName("annee")
    private int annee;


    @SerializedName("ticket_type")
    private String ticketType;

    @SerializedName("status")
    private String status;

    @SerializedName("entity_model")
    private String entityModel;   // ForeignKey → en général, Django renvoie un ID

    @SerializedName("user")
    private int user;          // idem, ID utilisateur

    @SerializedName("coord")
    private String coord;

    @SerializedName("commentaire")
    private String commentaire;

    public Paiement(int user, String entityModel) {
        this.user = user;
        this.entityModel = entityModel;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEntityModel() {
        return entityModel;
    }

    public void setEntityModel(String entityModel) {
        this.entityModel = entityModel;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getCoord() {
        return coord;
    }

    public void setCoord(String coord) {
        this.coord = coord;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }


    public int getAnnee() {
        return annee;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }


    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
