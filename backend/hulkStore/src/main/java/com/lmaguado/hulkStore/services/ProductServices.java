package com.lmaguado.hulkStore.services;

import com.lmaguado.hulkStore.models.GeneralResponsiveModel;
import com.lmaguado.hulkStore.models.implement.httpRequest.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProductServices {
    @Autowired
    private transient QueriesServices queriesServices;

    public void buyProduct(GeneralResponsiveModel generalResponsiveModel, String code, ProductModel.BuyProduct filter) {
        if (validateCodeNotEmpty(generalResponsiveModel, code) && queriesServices.isProductExists(code)) {

        }
    }

    private boolean validateCodeNotEmpty(GeneralResponsiveModel generalResponsiveModel, String code) {
        if (code != null && code.length() > 0) return true;
        GeneralResponsiveModel.updateGeneralModel(generalResponsiveModel, HttpStatus.BAD_REQUEST, 0, "The code is empty.", null);
        return false;
    }

    private boolean validateIfProductExists(){
        return false;
    }
}
