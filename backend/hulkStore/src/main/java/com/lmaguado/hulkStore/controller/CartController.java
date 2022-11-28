package com.lmaguado.hulkStore.controller;

import com.lmaguado.hulkStore.models.GeneralResponsiveModel;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/cart")
public class CartController {
    @GetMapping()
    public GeneralResponsiveModel get() {
        GeneralResponsiveModel generalResponsiveModel = new GeneralResponsiveModel();
        return generalResponsiveModel;
    }

    @PostMapping("")
    public GeneralResponsiveModel save(@RequestParam("code") String code, @RequestParam("units") Integer unit) {
        GeneralResponsiveModel generalResponsiveModel = new GeneralResponsiveModel();
        System.out.println(code + " - " + unit);
        return generalResponsiveModel;
    }

    @DeleteMapping("/{code}")
    public GeneralResponsiveModel delete(@PathVariable("code") String code) {
        GeneralResponsiveModel generalResponsiveModel = new GeneralResponsiveModel();
        System.out.println(code);
        return generalResponsiveModel;
    }
}
