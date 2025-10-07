package com.gouandiaka.market.utils;

import com.gouandiaka.market.entity.Entity;

import java.util.List;

public interface RequestListener {

    void onSuccess(boolean success,List<Entity> entities);

}
