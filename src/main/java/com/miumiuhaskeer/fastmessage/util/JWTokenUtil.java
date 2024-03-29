package com.miumiuhaskeer.fastmessage.util;

import com.miumiuhaskeer.fastmessage.properties.config.JWTokenProp;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JWTokenUtil {

    public static final String BEARER_PREFIX = "Bearer ";

    private final JWTokenProp jwTokenProp;
    private final UserDetailsService userDetailsService;

    /**
     * Generate token by email
     *
     * @param email user email (used as username)
     * @return new token
     * @throws UsernameNotFoundException if the user could not be found or the user has no GrantedAuthority
     */
    public String generateToken(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        return generateToken(userDetails);
    }

    /**
     * Generate token for FMS service by email. Can call only when user request FMS server
     *
     * @param email user email (used as username, must be existing)
     * @return new token
     */
    public String generateFMSToken(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Date current = new Date();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(current)
                .setExpiration(new Date(current.getTime() + jwTokenProp.getFmsExpirationSeconds() * 1000))
                .signWith(SignatureAlgorithm.HS512, jwTokenProp.getFmsSecret())
                .compact();
    }

    /**
     * Generate token by email
     *
     * @param userDetails details about user
     * @return new token
     */
    public String generateToken(UserDetails userDetails) {
        Date current = new Date();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(current)
                .setExpiration(new Date(current.getTime() + jwTokenProp.getExpirationSeconds() * 1000))
                .signWith(SignatureAlgorithm.HS512, jwTokenProp.getSecret())
                .compact();
    }

    /**
     * Get email from token. You can use validateToken method to prevent any Runtime exceptions
     *
     * @param token jwt string token
     * @return user email
     * @throws UnsupportedJwtException if the token argument does not represent an Claims JWS
     * @throws MalformedJwtException if the token string is not a valid JWS
     * @throws SignatureException if the token JWS signature validation fails
     * @throws ExpiredJwtException if the specified JWT is a Claims JWT and the Claims has an expiration time
     *                             before the time this method is invoked.
     * @throws IllegalArgumentException if the token string is null or empty or only whitespace
     * @see JWTokenUtil#validateToken(String)
     */
    public String getEmailFromToken(String token) {
        return getClaimsFromToken(token).getBody().getSubject();
    }

    /**
     * Return true if token is valid or false if not. If method getClaimsFromToken throw any exception,
     * it means that token is not valid
     *
     * @param token jwt string token
     * @return true - token is valid or
     *         false - token is not valid
     * @see JWTokenUtil#getClaimsFromToken(String)
     */
    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Generates header by token. Header name is "Authorization"
     *
     * @param token raw token for user
     * @return bearer token
     */
    public String generateHeader(String token) {
        return BEARER_PREFIX + token;
    }

    public boolean headerIsToken(String header) {
        return header != null && header.startsWith(BEARER_PREFIX);
    }

    /**
     * Get token from header by removing bearer prefix
     *
     * @param header string bearer token
     * @return token without bearer prefix
     * @throws IndexOutOfBoundsException if bearer prefix is not in header, check this by headerIsToken method
     * @see JWTokenUtil#headerIsToken(String)
     */
    public String getTokenFromHeader(String header) {
        return header.substring(BEARER_PREFIX.length());
    }

    /**
     * Get info from token
     *
     * @param token jwt string token
     * @return info from token
     * @throws UnsupportedJwtException if the token argument does not represent an Claims JWS
     * @throws MalformedJwtException if the token string is not a valid JWS
     * @throws SignatureException if the token JWS signature validation fails
     * @throws ExpiredJwtException if the specified JWT is a Claims JWT and the Claims has an expiration time
     *                             before the time this method is invoked.
     * @throws IllegalArgumentException if the token string is null or empty or only whitespace
     */
    private Jws<Claims> getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(jwTokenProp.getSecret()).parseClaimsJws(token);
    }
}
