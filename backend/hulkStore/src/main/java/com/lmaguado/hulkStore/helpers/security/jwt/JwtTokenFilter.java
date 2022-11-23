package com.lmaguado.hulkStore.helpers.security.jwt;

import com.lmaguado.hulkStore.helpers.security.dto.SecureUserDTO;
import com.lmaguado.hulkStore.helpers.security.service.UserDetailsServiceImp;
import com.lmaguado.hulkStore.services.QueriesServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserDetailsServiceImp detailsService;
    @Autowired
    private QueriesServices queriesServices;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(request);
            if (
                    token != null &&
                    queriesServices.getTokenBlacklist(token).isEmpty() &&
                    jwtProvider.validateToken(token)
            ) {
                String username = jwtProvider.getUserNameFromToken(token);
                UserDetails userDetails = detailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            //String errMsg = ">> JwtTokenFilter:\n" + e;
            //LOGGER.error(errMsg);
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer")) return header.replace("Bearer ", "");
        return null;
    }
}
