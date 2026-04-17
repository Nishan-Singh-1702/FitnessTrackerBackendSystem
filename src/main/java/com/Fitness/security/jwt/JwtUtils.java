package com.Fitness.security.jwt;

import com.Fitness.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${spring.app.jwtExpirationMS}")
    private Integer jwtExpirationMS;

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtCookie}")
    private String jwtCookie;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    public String GetJwtTokenFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Authorization Header: {}",bearerToken);
        if(bearerToken!=null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    public String getJwtTokenFromCookies(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if(cookie!=null){
            return cookie.getValue();
        }
        else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userDetails){
        String jwtToken = generateJwtTokenFromUserName(userDetails);
        ResponseCookie cookie = ResponseCookie.from(jwtCookie,jwtToken)
                .path("/api")
                .maxAge(24*60*60)
                .httpOnly(false)
                .build();
        return cookie;
    }

    public ResponseCookie generateCleanCookie(){
        return ResponseCookie.from(jwtCookie,null)
                .path("/api")
                .build();
    }


    public String generateJwtTokenFromUserName(UserDetails userDetails){
        String username = userDetails.getUsername();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpirationMS))
                .signWith(key())
                .compact();
    }

    public String getUserNameFromJwtToken(String token){
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private SecretKey key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken){
        try{
            System.out.println("Authorized JWT: ");
            Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        }
        catch(MalformedJwtException e){
            logger.error("JWT Token is Invalid: {}",e.getMessage());
        }
        catch(UnsupportedJwtException e){
            logger.error("JWT Token is Unsupported: {}",e.getMessage());
        }
        catch(ExpiredJwtException e){
            logger.error("JWT Token is Expired: {}",e.getMessage());
        }
        catch(IllegalArgumentException e){
            logger.error("JWT Token claims String is empty: {}",e.getMessage());
        }
        return false;
    }
}
