package com.lmaguado.hulkStore.helpers.security.jwt;

import com.lmaguado.hulkStore.helpers.fileManager.FileManager;
import com.lmaguado.hulkStore.helpers.fileManager.FileManagerException;
import com.lmaguado.hulkStore.helpers.security.dto.SecureUserDTO;
import com.lmaguado.hulkStore.helpers.strings.Constants;
import com.lmaguado.hulkStore.services.QueriesServices;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtProvider {
    private final FileManager fileManager;
    @Autowired
    private QueriesServices queriesServices;
    private Date issuedAt;
    private String secret;
    private long expiration;

    @Autowired
    public JwtProvider(FileManager fileManager, QueriesServices queriesServices) {
        this.fileManager = fileManager;
    }

    @PostConstruct
    public void postJwtProvider() throws FileManagerException {
        secret = fileManager.getPropertyByKey(Constants.PROPERTIES_JWT_NAME, "secret");
        issuedAt = new Date();
        expiration = issuedAt.getTime() + Long.parseLong(fileManager.getPropertyByKey(Constants.PROPERTIES_JWT_NAME, "expirationTime")) * 1000;
    }

    public String generateToken(Authentication authentication) {
        SecureUserDTO secureUserDTO = (SecureUserDTO) authentication.getPrincipal();
        // mapping header
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        // load audience
        String[] audience = new String[secureUserDTO.getAuthorities().size()];
        List<?> list = secureUserDTO.getAuthorities().stream().toList();
        for (int i = 0; i < secureUserDTO.getAuthorities().size(); i++) audience[i] = list.get(i).toString();
        // load mapper
        JWTCreator.Builder builder = JWT.create().withHeader(map);
        // load user date
        builder.withSubject(secureUserDTO.getUsername());
        builder.withIssuedAt(issuedAt);
        builder.withExpiresAt(new Date(expiration));
        builder.withAudience(audience);
        // return
        return builder.sign(Algorithm.HMAC256(secret));
    }

    public String getUserNameFromToken(String token) {
        JWTVerifier verification = JWT.require(Algorithm.HMAC256(secret)).build();
        return verification.verify(token).getSubject();
    }

    public boolean validateToken(String token) throws TokenExpiredException {
        try {
            JWTVerifier verification = JWT.require(Algorithm.HMAC256(secret)).build();
            verification.verify(token);
            return true;
        } catch (AlgorithmMismatchException | SignatureVerificationException | InvalidClaimException e) {
            System.out.println(e);
            return false;
        }
    }
}
