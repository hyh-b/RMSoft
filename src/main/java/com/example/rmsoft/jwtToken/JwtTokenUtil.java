package com.example.rmsoft.jwtToken;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
@Service
public class JwtTokenUtil {
    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private long expirationTime = 1000 * 60 * 60; // 토큰 유효 시간
    // 토큰 생성
    public String generateToken(String memberId, String email) {
        return Jwts.builder()
                .setSubject(memberId)
                .claim("email", email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
    // 토큰 유효성 확인 및 유저 아이디 확인
    public String validateTokenAndGetMemberId(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return claims.getBody().getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

}
