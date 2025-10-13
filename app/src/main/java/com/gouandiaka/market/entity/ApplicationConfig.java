package com.gouandiaka.market.entity;

import com.gouandiaka.market.utils.Constant;
import com.gouandiaka.market.utils.PrefUtils;
import com.gouandiaka.market.utils.Validator;

import java.util.Calendar;
import java.util.Objects;

public class ApplicationConfig {

    public static final String COMMUNE = "150202";
    private String city;

    private int user;

    public int getUser() {
        return user;
    }

    private String userName;

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUser(int user) {
        this.user = user;
    }

    private long lastTime;


    private String locality;

    private String commune;

    public String getCommune() {
        return commune;
    }

    private int annee;

    private String mois;

    private String ticketType;

    private String property;


    public String getProperty() {
        return property;
    }

    public static ApplicationConfig getDefault() {
        ApplicationConfig config = new ApplicationConfig();
        config.city = "Kalana";
        config.annee = Calendar.getInstance().get(Calendar.YEAR);
        config.mois = Constant.MOIS_MAP.get(Calendar.getInstance().get(Calendar.MONTH));
        config.ticketType = "TK500";
        config.property = "PRIVEE";
        config.user = 0;
        config.lastTime = 0;
        return config;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLocality(String locality) {
        this.locality = locality;
        if("ESPACE PUBLIC".equalsIgnoreCase(property)){
            this.locality= "Kalana";
        }
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public void setProperty(String property) {
        this.property = property;
        if("ESPACE PUBLIC".equalsIgnoreCase(property)){
            this.locality= "Kalana";
        }

    }



    public String getCity() {
        return city;
    }

    public String getLocality() {
        if("ESPACE PUBLIC".equalsIgnoreCase(property)){
            return "Kalana";
        }
        return locality;
    }

    public int getAnnee() {
        return annee;
    }

    public String getMois() {
        return mois;
    }

    public String getTicketType() {
        return ticketType;
    }

    public  Entity getEntity() {
        Entity model = new Entity(commune, city,locality, property, user);
        if(Validator.isValid(model))return model;
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationConfig that = (ApplicationConfig) o;
        return annee == that.annee && Objects.equals(locality, that.locality) && Objects.equals(mois, that.mois) && Objects.equals(ticketType, that.ticketType) && Objects.equals(property, that.property);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locality, annee, mois, ticketType, property);
    }

    public  Paiement getPaiement(String entity){
        Paiement paiement = new Paiement(user, entity,annee);
        paiement.setAnnee(annee);
        paiement.setPeriod(mois);
        paiement.setTicketType(ticketType);
        return paiement;
    }
}
