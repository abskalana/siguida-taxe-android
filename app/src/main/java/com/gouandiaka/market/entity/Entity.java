package com.gouandiaka.market.entity;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.gouandiaka.market.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Entity {

    private String id;
    private final String commune;

    private int numero;
    public String getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    private final String city;

    public String getContactPhone() {
        return contactPhone;
    }

    public String getCoord() {
        return coord;
    }

    private final String locality;
    private String activity;
    private String property = "PRIVEE";

    @SerializedName("contact_nom")
    private String contactNom;
    @SerializedName("contact_prenom")
    private String contactPrenom;
    @SerializedName("contact_phone")
    private String contactPhone;

    @SerializedName("type_entity")
    private String typeEntity = "MAISON";

    private int porte = 1;

    private String coord;

    private String status = "OUVERT";
    @SerializedName("status_paiement")
    private String paiementStatus;



    private int user;


    public Entity(String commune, String city, String locality, String property, int user) {
        this.commune = commune;
        this.city = city;
        this.locality = locality;
        this.user = user;
        this.property = property;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setTypeProperty(String typeEntity) {
        this.typeEntity = typeEntity;
    }


    public void setContactName(String contactName) {
        this.contactNom = contactName;
    }


    public void setContactPrenom(String contactPrenom) {
        this.contactPrenom = contactPrenom;
    }


    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }


    public void setPorte(int porte) {
        this.porte = porte;
    }


    public void setCoord(String coord) {
        this.coord = coord;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isInCorrect() {
        return Utils.isEmpty(commune) || Utils.isEmpty(city) || Utils.isEmpty(property) || user < 0;
    }

    @NonNull
    public String toString() {
        return this.contactNom.trim() + "," + this.contactPrenom.trim() + "," + this.contactPhone.trim();
    }

    public String getCommune() {
        return commune;
    }

    public String getCity() {
        return city;
    }

    public String getLocality() {
        return locality;
    }

    public String getProperty() {
        return property;
    }

    public String getContactNom() {
        return contactNom;
    }

    public String getContactPrenom() {
        return contactPrenom;
    }

    public String getTypeEntity() {
        return typeEntity;
    }

    public int getPorte() {
        return porte;
    }

    public String getStatus() {
        return status;
    }

    public int getUser() {
        return user;
    }

    public String getActivity() {
        return this.activity;
    }

    public String getNomComplet() {
        return this.contactPrenom + " " + this.contactNom.toUpperCase() + " #"+this.numero;
    }

    public String getNomCompletTelephone() {
        return this.contactPrenom + " " + this.contactNom.toUpperCase() + " #"+this.getTelephone1();
    }

    public String getTelephone1() {
        if (Utils.isEmpty(this.contactPhone)) return null;
        if (this.contactPhone.split(",").length > 0) return this.contactPhone.split(",")[0];
        return null;
    }

    public String getTelephone2() {
        if (Utils.isEmpty(this.contactPhone)) return null;
        if (this.contactPhone.split(",").length > 1) return this.contactPhone.split(",")[1];
        return null;
    }

    public String getPaiementStatus() {
        return paiementStatus;
    }

    public void setPaiementStatus(String paiementStatus) {
        this.paiementStatus = paiementStatus;
    }

    public String getInfo() {
        return "#Porte : " + this.porte + " -  " + this.property.toUpperCase() + "  -  " + this.activity;
    }

    public String toCsvRow() {
        return String.join(",",
                city,
                locality,
                safe(activity),
                property,
                safe(contactNom),
                safe(contactPrenom),
                safe(contactPhone),
                typeEntity,
                String.valueOf(porte),
                safe(coord),
                status,
                String.valueOf(user)
        );
    }

    // Prevent null issues
    private String safe(String value) {
        return value == null ? "" : value;
    }

    public static List<Entity> parseList(String response){
        try{
            List<Entity> entities = new ArrayList<>();
            Type listType = new TypeToken<List<Entity>>() {
            }.getType();
            entities = new Gson().fromJson(response, listType);
            if(entities == null || entities.isEmpty()) return null;
            return entities;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(id, entity.id) && Objects.equals(commune, entity.commune);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, commune);
    }

    public boolean is_paie(){
        if(this.paiementStatus == null) return false;
        return  "PAYÉ".equalsIgnoreCase(this.paiementStatus) ||"PAYE_MAIRIE".equalsIgnoreCase(this.paiementStatus);
    }

    public static boolean isValidEntity(Entity entity){
        if(entity == null || entity.id == null) return false;
        return !entity.isInCorrect();
    }
}


