package com.lmaguado.hulkStore.services;

import com.lmaguado.hulkStore.helpers.security.dto.JwtDTO;
import com.lmaguado.hulkStore.helpers.security.jwt.JwtProvider;
import com.lmaguado.hulkStore.helpers.strings.GeneralResponsiveMessage;
import com.lmaguado.hulkStore.models.GeneralResponsiveModel;
import com.lmaguado.hulkStore.models.implement.httpRequest.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {
    @Autowired
    private QueriesServices queriesServices;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;

    public void createNewUserAccount(GeneralResponsiveModel responsive, UserModel.UserModelBasic user) {
        if (isUserRegisterDataEmpty(responsive, user) || isUserExist(responsive, queriesServices.isUserExists(user.getEmail()))) return;
        createUser(responsive, queriesServices.createUser(user.getEmail(), passwordEncoder.encode(user.getPassword())));
    }

    private boolean isUserRegisterDataEmpty(@NonNull GeneralResponsiveModel responsive, UserModel.UserModelBasic user) {
        if (
                user.getEmail() != null
                && user.getPassword() != null
                && user.getEmail().length() > 0
                && user.getPassword().length() > 0
        ) return false;
        responsive.setStatus(HttpStatus.OK);
        responsive.setCode(0);
        responsive.setMessage(GeneralResponsiveMessage.USER_DATA_IS_NULL);
        responsive.setData(null);
        return true;
    }

    private boolean isUserExist (@NonNull GeneralResponsiveModel responsive, long consult) {
        boolean userExist = consult > 0;
        responsive.setStatus(HttpStatus.OK);
        responsive.setCode(0);
        responsive.setMessage(GeneralResponsiveMessage.USER_DOES_NOT_EXIST);
        if (userExist) responsive.setMessage(GeneralResponsiveMessage.USER_ALREADY_EXISTS);
        responsive.setData(null);
        return userExist;
    }

    private void createUser(@NonNull GeneralResponsiveModel responsive, long consult) {
        if (consult > 0) {
            responsive.setStatus(HttpStatus.OK);
            responsive.setCode(1);
            responsive.setMessage(GeneralResponsiveMessage.USER_CREATED_SUCCESSFULLY);
            responsive.setData(null);
        }
    }

    public void loginUserAccount(GeneralResponsiveModel responsive, UserModel.UserModelBasic user) {
        if (isUserLoginDataEmpty(responsive, user) || !isUserExist(responsive, queriesServices.isUserExists(user.getEmail()))) return;
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtDTO jwtDTO = new JwtDTO(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        // Http response
        responsive.setStatus(HttpStatus.OK);
        responsive.setCode(1);
        responsive.setMessage(GeneralResponsiveMessage.USER_LOGIN_SUCCESSFULLY);
        responsive.setData(jwtDTO);
    }

    private boolean isUserLoginDataEmpty(@NonNull GeneralResponsiveModel responsive, UserModel.UserModelBasic user) {
        if (
                user.getEmail() != null
                        && user.getPassword() != null
                        && user.getEmail().length() > 0
                        && user.getPassword().length() > 0
        ) return false;
        responsive.setStatus(HttpStatus.OK);
        responsive.setCode(0);
        responsive.setMessage(GeneralResponsiveMessage.USER_DATA_IS_NULL);
        responsive.setData(null);
        return true;
    }
}
