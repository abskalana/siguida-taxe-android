package com.gouandiaka.market.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

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

    @SerializedName("date_created")
    private String date;

    public Paiement(int user, String entityModel, int annee) {
        this.user = user;
        this.entityModel = entityModel;
        this.annee = annee;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Paiement paiement = (Paiement) o;
        if(id==null && paiement.id == null){
            return annee == paiement.annee && Objects.equals(mois, paiement.mois) && Objects.equals(period, paiement.period) && Objects.equals(status, paiement.status) && Objects.equals(entityModel, paiement.entityModel);
         }else{
           return Objects.equals(id, paiement.id);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mois, period, annee, status, entityModel);
    }

    public void setId(String id) {
        this.id = id;
    }


}
