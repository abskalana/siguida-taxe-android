package com.gouandiaka.market.entity;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.gouandiaka.market.utils.Utils;

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
    @SerializedName("paiement")
    private Paiement paiement;



    private final int user;


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
        this.contactNom = Utils.capitalizeFirst(contactName);
    }


    public void setContactPrenom(String contactPrenom) {
        this.contactPrenom = Utils.capitalizeFirst(contactPrenom);
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
        return Utils.capitalizeFirst(contactNom);
    }

    public String getContactPrenom() {
        return Utils.capitalizeFirst(contactPrenom);
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



    public String getTelephone1() {
        if (Utils.isEmpty(this.contactPhone)) return null;
        return this.contactPhone;
    }


    public Paiement getPaiement() {
        return paiement;
    }

    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }

    public String getInfo() {
        return "#Porte : " + this.porte + " -  " + this.property.toUpperCase() + "  -  " + this.activity;
    }


    public static Entity parseList(String response){
        try{
            Entity entity=  new Gson().fromJson(response, Entity.class);
            if(entity!=null && !Utils.isEmpty(entity.getId())){
                return entity;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        if(id == null && entity.id == null) return Objects.equals(contactPhone, entity.contactPhone);
        return Objects.equals(id, entity.id) && Objects.equals(contactPhone, entity.contactPhone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contactPhone);
    }

    public boolean is_paie(){
        if(this.paiement == null) return false;
        return  "PAYÉ".equalsIgnoreCase(this.paiement.getStatus()) ||"PAYE_MAIRIE".equalsIgnoreCase(this.paiement.getStatus());
    }

    public String getPaiementStatus() {
        if(paiement == null|| Utils.isEmpty(paiement.getStatus())) return "NON_DEMANDÉ";
        return paiement.getStatus();
    }
}


