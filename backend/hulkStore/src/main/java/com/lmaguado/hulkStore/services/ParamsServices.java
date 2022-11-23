package com.lmaguado.hulkStore.services;

import com.lmaguado.hulkStore.helpers.strings.GeneralResponsiveMessage;
import com.lmaguado.hulkStore.models.GeneralResponsiveModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ParamsServices {
    @Autowired
    private QueriesServices queriesServices;

    public void getValueParam(GeneralResponsiveModel responsive, String key) {
        if (isKeyValueEmpty(responsive, key)) return;
        responsive.setStatus(HttpStatus.OK);
        responsive.setCode(1);
        responsive.setMessage(queriesServices.getGlobalParam(key));
        responsive.setData(null);
    }

    private boolean isKeyValueEmpty(GeneralResponsiveModel responsive, String key) {
        if (key != null && key.length() > 0) return false;
        responsive.setStatus(HttpStatus.OK);
        responsive.setCode(0);
        responsive.setMessage(GeneralResponsiveMessage.PARAMS_KEY_IS_NULL);
        responsive.setData(null);
        return true;
    }
}
