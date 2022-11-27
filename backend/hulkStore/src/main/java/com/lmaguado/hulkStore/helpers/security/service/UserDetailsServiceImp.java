package com.lmaguado.hulkStore.helpers.security.service;

import com.lmaguado.hulkStore.helpers.security.dto.SecureUserDTO;
import com.lmaguado.hulkStore.models.implement.dto.database.GetUserDTO;
import com.lmaguado.hulkStore.models.implement.dto.database.GetUserPassDTO;
import com.lmaguado.hulkStore.services.QueriesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImp implements UserDetailsService {
    @Autowired
    private QueriesServices queriesServices;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GetUserDTO> userList = queriesServices.getUserByUserName(username);
        if (!userList.isEmpty()) {
            List<GetUserPassDTO> passList = queriesServices.getUserPass(userList.get(0).getUserId());
            if (!passList.isEmpty()) {
                return SecureUserDTO.build(
                        userList.get(0),
                        passList.get(0),
                        queriesServices.getPermissions()
                );
            }
        }
        return null;
    }
}
