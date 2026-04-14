package com.example.demo.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * JWT 토큰 생성 / 파싱 / 검증 담당
 *
 * JWT 구조: Header.Payload.Signature
 *   Header:    알고리즘 정보 (HS256)
 *   Payload:   subject(loginId), 발급시간, 만료시간
 *   Signature: secret key로 서명 → 위변조 방지
 *
 * 토큰 예시:
 *   eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJob25nMTIzIn0.xxxx
 */
@Component
public class JwtTokenProvider {

    /**
     * 서명에 사용할 Key 객체
     * application.yml의 jwt.secret 값으로 생성
     */
    private final Key key;

    /**
     * 토큰 만료 시간 (밀리초)
     * application.yml의 jwt.expiration 값
     */
    private final long expiration;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration) {
        // secret 문자열을 HMAC-SHA 알고리즘용 Key 객체로 변환
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    /**
     * JWT 토큰 생성
     * @param loginId 토큰의 주인 식별자 (Payload의 subject에 저장)
     * @return 서명된 JWT 문자열
     */
    public String generateToken(String loginId) {
        return Jwts.builder()
                .setSubject(loginId)                                              // 토큰 주인
                .setIssuedAt(new Date())                                          // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256)                          // 서명
                .compact();
    }

    /**
     * 토큰에서 loginId 추출
     * @param token JWT 문자열
     * @return Payload의 subject (loginId)
     */
    public String getLoginIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)    // 서명 검증에 사용할 key
                .build()
                .parseClaimsJws(token) // 파싱 + 서명 검증
                .getBody()
                .getSubject();         // subject = loginId
    }

    /**
     * 토큰 유효성 검증
     * 만료, 위조, 형식 오류 등 모든 예외를 catch해서 false 반환
     * @return 유효하면 true, 아니면 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);  // 예외 없이 파싱되면 유효한 토큰
            return true;
        } catch (ExpiredJwtException e) {
            // 토큰 만료
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            // 위조, 형식 오류 등등
            return false;
        }
    }

    public LocalDateTime getExpirationFromToken(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        //JWT 는 Date 를 고정으로 주기 때문에 LocalDateTime 으로 변환시켜줘야함
        // 다른것도 더있다고함
        return expiration.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();
    }
}