package com.lmaguado.hulkStore.controller;

import com.lmaguado.hulkStore.models.GeneralResponsiveModel;
import com.lmaguado.hulkStore.models.implement.httpRequest.ProductModel;
import com.lmaguado.hulkStore.services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private transient ProductServices productServices;

    @PostMapping("/buy/{code}")
    public GeneralResponsiveModel buyProduct(@PathVariable("code") String code, @RequestBody ProductModel.BuyProduct filter) {
        GeneralResponsiveModel generalResponsiveModel = new GeneralResponsiveModel();
        productServices.buyProduct(generalResponsiveModel, code, filter);
        return generalResponsiveModel;
    }
}
