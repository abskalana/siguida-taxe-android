package com.gouandiaka.market.utils;

import com.gouandiaka.market.entity.Entity;

public interface RequestListener {

    void onSuccess(boolean success,Entity  entity);

}
