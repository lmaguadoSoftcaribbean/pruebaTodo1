package com.lmaguado.hulkStore.controller;

import com.lmaguado.hulkStore.models.GeneralResponsiveModel;
import com.lmaguado.hulkStore.models.implement.httpRequest.ProductModel;
import com.lmaguado.hulkStore.services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private transient ProductServices productServices;

    @GetMapping("")
    public GeneralResponsiveModel getProducts() {
        GeneralResponsiveModel generalResponsiveModel = new GeneralResponsiveModel();
        productServices.getProducts(generalResponsiveModel);
        return generalResponsiveModel;
    }

    @PostMapping("")
    public GeneralResponsiveModel getProductsFilter(@RequestBody ProductModel.ProductFilter filter) {
        GeneralResponsiveModel generalResponsiveModel = new GeneralResponsiveModel();
        productServices.getProducts(generalResponsiveModel, filter);
        return generalResponsiveModel;
    }

    @GetMapping("/{code}")
    public GeneralResponsiveModel getProduct(@PathVariable("code") String code) {
        GeneralResponsiveModel generalResponsiveModel = new GeneralResponsiveModel();
        productServices.getProduct(generalResponsiveModel, code);
        return generalResponsiveModel;
    }

    @PostMapping("/buy/{code}")
    public GeneralResponsiveModel buyProduct(@PathVariable("code") String code, @RequestBody ProductModel.ProductFilterBuy filter) {
        GeneralResponsiveModel generalResponsiveModel = new GeneralResponsiveModel();
        productServices.buyProduct(generalResponsiveModel, code, filter);
        return generalResponsiveModel;
    }

    @PostMapping("/replenish/{code}")
    public GeneralResponsiveModel replenishProduct(@PathVariable("code") String code, @RequestBody ProductModel.ProductFilterReplenish filter) {
        GeneralResponsiveModel generalResponsiveModel = new GeneralResponsiveModel();
        productServices.replenishProduct(generalResponsiveModel, code, filter);
        return generalResponsiveModel;
    }
}