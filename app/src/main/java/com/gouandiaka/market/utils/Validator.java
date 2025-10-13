package com.gouandiaka.market.utils;

import com.gouandiaka.market.entity.ApplicationConfig;
import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.entity.Paiement;

public class Validator {
    public static boolean isValid(Entity entity){
        if(entity == null) return false;
        return !Utils.isEmpty(entity.getCommune()) && !Utils.isEmpty(entity.getCity()) && !Utils.isEmpty(entity.getProperty()) && !Utils.isEmpty(entity.getLocality()) && entity.getUser() >= 0;

    }
    public static boolean isValid(Paiement paiement){
        if(paiement == null) return false;
        if (paiement.getUser()<0 || paiement.getValue() < 100 || paiement.getAnnee() < 2025 || Utils.isEmpty(paiement.getEntityModel()))
            return false;
        return !Utils.isEmpty(paiement.getMois()) && !Utils.isEmpty(paiement.getTicketType());
    }

    public static boolean isValid(ApplicationConfig config){
        if(config == null) return false;
        if(Utils.isSelectOrEmpty(config.getCommune())) return false;
        if(Utils.isSelectOrEmpty(config.getCity())) return false;
        if(Utils.isSelectOrEmpty(config.getLocality())) return false;
        if(Utils.isSelectOrEmpty(config.getMois())) return false;
        if(Utils.isSelectOrEmpty(config.getProperty())) return false;
        return config.getAnnee() >= 2024 && config.getAnnee() <= 2050;
    }

    public static boolean isValidRemote(Entity entity){
        return isValid(entity) && !Utils.isEmpty(entity.getId());
    }

}
