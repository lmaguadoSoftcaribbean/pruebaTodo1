package com.lmaguado.hulkStore.helpers.security.dto;

import com.lmaguado.hulkStore.models.implement.dto.database.GetPermissionDTO;
import com.lmaguado.hulkStore.models.implement.dto.database.GetUserDTO;
import com.lmaguado.hulkStore.models.implement.dto.database.GetUserPassDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SecureUserDTO implements UserDetails {
    private final GetUserDTO user;
    private final GetUserPassDTO pass;
    private final Collection<? extends GrantedAuthority> authorities;

    public SecureUserDTO(GetUserDTO user, GetUserPassDTO pass, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.pass = pass;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return pass.getUserPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public GetUserDTO getUser() {
        return user;
    }

    public static SecureUserDTO build(GetUserDTO user, GetUserPassDTO pass, List<GetPermissionDTO> listPermit) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        listPermit.forEach(role -> {
            if (role.getPermitWeight() <= user.getPermWeight()) authorities.add(new SimpleGrantedAuthority(role.getPermitLabel()));
        });
        return new SecureUserDTO(user, pass, authorities);
    }
}
