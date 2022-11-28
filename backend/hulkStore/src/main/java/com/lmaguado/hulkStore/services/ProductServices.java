package com.lmaguado.hulkStore.services;

import com.lmaguado.hulkStore.helpers.strings.GeneralResponsiveMessage;
import com.lmaguado.hulkStore.models.GeneralResponsiveModel;
import com.lmaguado.hulkStore.models.implement.dto.database.GetProductDTO;
import com.lmaguado.hulkStore.models.implement.httpRequest.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServices {
    @Autowired
    private transient QueriesServices queriesServices;

    private boolean validateCodeNotEmpty(GeneralResponsiveModel generalResponsiveModel, String code) {
        if (code != null && code.length() > 0) return true;
        GeneralResponsiveModel.updateGeneralModel(generalResponsiveModel, HttpStatus.BAD_REQUEST, 0, GeneralResponsiveMessage.PRODUCT_CODE_IS_EMPTY, null);
        return false;
    }

    private boolean validateIfProductExists(GeneralResponsiveModel generalResponsiveModel, String code) {
        if (queriesServices.isProductExists(code)) return true;
        GeneralResponsiveModel.updateGeneralModel(generalResponsiveModel, HttpStatus.OK, 0, GeneralResponsiveMessage.PRODUCT_CANT_BE_FOUND, null);
        return false;
    }

    public void getProduct(GeneralResponsiveModel generalResponsiveModel, String code) {
        if (!validateCodeNotEmpty(generalResponsiveModel, code) || !validateIfProductExists(generalResponsiveModel, code)) return;
        List<GetProductDTO> productos = queriesServices.getProduct(code);
        GeneralResponsiveModel.updateGeneralModel(generalResponsiveModel, HttpStatus.OK, 1, GeneralResponsiveMessage.REQUEST_SUCCESS, productos);
    }

    public void getProducts(GeneralResponsiveModel generalResponsiveModel) {
        List<GetProductDTO> productos = queriesServices.getAllProducts();
        GeneralResponsiveModel.updateGeneralModel(generalResponsiveModel, HttpStatus.OK, 1, GeneralResponsiveMessage.REQUEST_SUCCESS, productos);
    }

    public void getProducts(GeneralResponsiveModel generalResponsiveModel, ProductModel.ProductFilter filter) {
        List<GetProductDTO> productos = queriesServices.getAllProducts();
        GeneralResponsiveModel.updateGeneralModel(generalResponsiveModel, HttpStatus.OK, 1, GeneralResponsiveMessage.REQUEST_SUCCESS, productos);
    }

    public void buyProduct(GeneralResponsiveModel generalResponsiveModel, String code, ProductModel.ProductFilterBuy filter) {
        if (!validateCodeNotEmpty(generalResponsiveModel, code) || !validateIfProductExists(generalResponsiveModel, code)) return;
        List<GetProductDTO> productos = queriesServices.getProduct(code);
        if (productos.isEmpty()) {
            GeneralResponsiveModel.updateGeneralModel(generalResponsiveModel, HttpStatus.OK, 0, GeneralResponsiveMessage.PRODUCT_CANT_BE_FOUND, null);
            return;
        }
        int stock = productos.get(0).getUnit();
        if (stock < filter.getUnits()) {
            GeneralResponsiveModel.updateGeneralModel(generalResponsiveModel, HttpStatus.OK, 0, GeneralResponsiveMessage.PRODUCT_OVERFLOW_UNIT_EXISTENT, null);
            return;
        }
        int units = stock - filter.getUnits();
        if (queriesServices.setProductUnit(code, units)) {
            GeneralResponsiveModel.updateGeneralModel(generalResponsiveModel, HttpStatus.OK, 1, GeneralResponsiveMessage.PRODUCT_SET_UNIT_SUCCESS, null);
        } else {
            GeneralResponsiveModel.updateGeneralModel(generalResponsiveModel, HttpStatus.OK, 0, GeneralResponsiveMessage.PRODUCT_SET_UNIT_ERROR, null);
        }
    }

    public void replenishProduct(GeneralResponsiveModel generalResponsiveModel, String code, ProductModel.ProductFilterReplenish filter) {
        int units = filter.getUnits();
        if (units <= 0) {
            GeneralResponsiveModel.updateGeneralModel(generalResponsiveModel, HttpStatus.OK, 0, GeneralResponsiveMessage.PRODUCT_MIN_1_UNIT, null);
            return;
        }
        if (!validateCodeNotEmpty(generalResponsiveModel, code) || !validateIfProductExists(generalResponsiveModel, code)) return;
        List<GetProductDTO> productos = queriesServices.getProduct(code);
        if (productos.isEmpty()) {
            GeneralResponsiveModel.updateGeneralModel(generalResponsiveModel, HttpStatus.OK, 0, GeneralResponsiveMessage.PRODUCT_CANT_BE_FOUND, null);
            return;
        }
        int storeUnits = productos.get(0).getUnit();
        if (queriesServices.setProductUnit(code, units + storeUnits)) {
            GeneralResponsiveModel.updateGeneralModel(generalResponsiveModel, HttpStatus.OK, 1, GeneralResponsiveMessage.PRODUCT_SET_UNIT_SUCCESS, null);
        } else {
            GeneralResponsiveModel.updateGeneralModel(generalResponsiveModel, HttpStatus.OK, 0, GeneralResponsiveMessage.PRODUCT_SET_UNIT_ERROR, null);
        }
    }
}
