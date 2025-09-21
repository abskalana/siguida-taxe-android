package com.gouandiaka.market.entity;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.gouandiaka.market.utils.Utils;

import java.util.Objects;

public class Entity {

    private String id;
    private final String  commune;

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(id, entity.id) && Objects.equals(commune, entity.commune) && Objects.equals(city, entity.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, commune, city);
    }

    private final String city;

    public String getContactPhone() {
        return contactPhone;
    }

    public String getCoord() {
        return coord;
    }

    private final String locality;
    private  String activity;
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


    private int user;


    public Entity(String commune, String city, String locality, String property, int user) {
        this.commune = commune;
        this.city = city;
        this.locality = locality;
        this.user = user;
        this.property = property;
    }

    public void setActivity(String activity) { this.activity = activity; }

    public void setTypeProperty(String typeEntity) { this.typeEntity = typeEntity; }


    public void setContactName(String contactName) { this.contactNom = contactName; }


    public void setContactPrenom(String contactPrenom) { this.contactPrenom = contactPrenom; }


    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }


    public void setPorte(int porte) { this.porte = porte; }



    public void setCoord(String coord) { this.coord = coord; }



    public void setStatus(String status) { this.status = status; }

    public boolean isInCorrect(){
        return Utils.isEmpty(commune) || Utils.isEmpty(city) || Utils.isEmpty(property) ||user <0;
    }

    @NonNull
    public String toString(){
        return this.contactNom.trim() + ","+this.contactPrenom.trim()+","+this.contactPhone.trim();
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
        return this.contactPrenom + " "+ this.contactNom.toUpperCase();
    }

    public String getTelephone1(){
        if(Utils.isEmpty(this.contactPhone)) return null;
        if(this.contactPhone.split(",").length > 0)return this.contactPhone.split(",")[0];
        return  null;
    }
    public String getTelephone2(){
        if(Utils.isEmpty(this.contactPhone)) return null;
        if(this.contactPhone.split(",").length > 1)return this.contactPhone.split(",")[1];
        return  null;
    }

    public String getInfo() {
        return "#Porte : " + this.porte + " -  "+ this.property.toUpperCase()+ "  -  "+ this.activity;
    }
}


