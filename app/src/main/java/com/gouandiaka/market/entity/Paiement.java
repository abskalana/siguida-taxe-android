package com.gouandiaka.market.entity;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.gouandiaka.market.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Paiement {

    @SerializedName("value")
    private int value;

    @SerializedName("mois")
    private String mois;

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


    public static boolean isValid(Paiement paiement) {
        if (paiement == null || Math.min(paiement.user, paiement.value) < 0 || Utils.isEmpty(paiement.entityModel))
            return false;
        return !Utils.isEmpty(paiement.entityModel) && !Utils.isEmpty(paiement.ticketType);
    }

    public String toCsvRow() {
        return String.join(",",
                String.valueOf(value),
                safe(mois),
                safe(ticketType),
                safe(status),
                safe(entityModel),
                String.valueOf(user),
                safe(coord),
                safe(commentaire)
        );
    }

    // Prevent nulls in CSV
    private String safe(String value) {
        return value == null ? "" : value;
    }


    public static List<Paiement> parseList(String response){
        try{
            List<Paiement> paiements = new ArrayList<>();
            Type listType = new TypeToken<List<Paiement>>() {
            }.getType();
            paiements = new Gson().fromJson(response, listType);
            if(paiements == null || paiements.isEmpty()) return null;
            return paiements;
        } catch (Exception e) {
            return null;
        }
    }

}
