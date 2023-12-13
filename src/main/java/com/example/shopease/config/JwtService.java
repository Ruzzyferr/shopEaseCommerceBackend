package com.example.shopease.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "l8JXuuDDsFB5Cp+uVI7VQ3Yh0ffjgWn8C4G5fWqw4bA=";
    private static final int validity = 60 * 60 * 1000;

    private Set<String> tokenBlacklist = new HashSet<>();

    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims,T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public String generateToken(
        String username,
        String role
    ){
        return Jwts
                .builder()
                .claim("role","ROLE_" + role)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token) && !isTokenBlacklisted(token);
    }


    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());

    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public String getRoleToken(String token) {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("role"); // Rol bilgisini token içinden çıkartıyoruz
    }

    public boolean tokenValidate(String token) {
        if (getUsernameToken(token) != null && isExpired(token) && !isTokenBlacklisted(token)) {
            return true;
        }
        return false;
    }

    public boolean isExpired(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration().after(new Date(System.currentTimeMillis()));
    }

    private Key getSignInKey() {

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public void invalidateToken(String token) {
        tokenBlacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        // Token "blacklist"te ise false dönecek
        return tokenBlacklist.contains(token);
    }

}
