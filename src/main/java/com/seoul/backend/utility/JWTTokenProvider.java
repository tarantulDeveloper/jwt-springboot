package com.seoul.backend.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.seoul.backend.constant.SecurityConstant;
import com.seoul.backend.domain.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class JWTTokenProvider {
    @Value("${jwt.secret}")
    private String secret;

    public String generateJwtToken(UserPrincipal userPrincipal) {
        String[] claims = getClaimsFromUser(userPrincipal);
        return JWT.create().withIssuer(SecurityConstant.GET_ARRAYS_LLC)
                .withAudience(SecurityConstant.GET_ARRAYS_ADMINISTRATION)
                .withIssuedAt(new Date()).withSubject(userPrincipal.getUsername())
                .withArrayClaim(SecurityConstant.AUTHORITIES,claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret.getBytes()));

    }

     public List<GrantedAuthority> getAuthorities(String token) {
        String[] claims = getClaimsFRomToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
     }

     private String[] getClaimsFRomToken(String token) {
         JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(SecurityConstant.AUTHORITIES)
                .asArray(String.class);
     }

     private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            verifier = JWT.require(algorithm).withIssuer(SecurityConstant.GET_ARRAYS_LLC).build();
        } catch(JWTVerificationException exception) {
            throw new JWTVerificationException(SecurityConstant.TOKEN_CANNOT_BE_VERIFIED);
        }
        return verifier;
     }

     public Authentication getAuthentication(String username,
                                             List<GrantedAuthority> authorities,
                                             HttpServletRequest request) {
         UsernamePasswordAuthenticationToken userPasswordAuthToken =
                 new UsernamePasswordAuthenticationToken(username, null, authorities);
         userPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
         return userPasswordAuthToken;
     }

    private String[] getClaimsFromUser(UserPrincipal user) {
        List<String> authorities = new ArrayList<>();
        for(GrantedAuthority grantedAuthority: user.getAuthorities()){
            authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }

    public boolean isTokenValid(String username, String token) {
        JWTVerifier verifier = getJWTVerifier();
        return 
    }

}
