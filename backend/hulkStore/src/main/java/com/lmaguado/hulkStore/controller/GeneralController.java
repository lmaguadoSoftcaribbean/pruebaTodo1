package com.lmaguado.hulkStore.controller;

import com.lmaguado.hulkStore.models.GeneralResponsiveModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneralController {
    @GetMapping("/unauthorized")
    public GeneralResponsiveModel unautorizedRoot() {
        GeneralResponsiveModel generalResponsiveModel = new GeneralResponsiveModel();
        generalResponsiveModel.setStatus(HttpStatus.UNAUTHORIZED);
        generalResponsiveModel.setCode(0);
        generalResponsiveModel.setMessage("Unauthorized");
        return generalResponsiveModel;
    }
}
