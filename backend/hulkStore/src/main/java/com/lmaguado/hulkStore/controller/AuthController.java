package com.lmaguado.hulkStore.controller;

import com.lmaguado.hulkStore.models.GeneralResponsiveModel;
import com.lmaguado.hulkStore.models.implement.httpRequest.UserModel;
import com.lmaguado.hulkStore.services.AuthServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthServices services;

    @PostMapping("/register")
    public GeneralResponsiveModel createAccount(@RequestBody UserModel.UserRegisterModel userModel) {
        GeneralResponsiveModel responsive = new GeneralResponsiveModel();
        services.createNewUserAccount(responsive, userModel);
        return responsive;
    }

    @PostMapping("/login")
    public GeneralResponsiveModel loginAccount(@RequestBody UserModel.UserLoginModel userModel) {
        GeneralResponsiveModel responsive = new GeneralResponsiveModel();
        services.loginUserAccount(responsive, userModel);
        return responsive;
    }
}
