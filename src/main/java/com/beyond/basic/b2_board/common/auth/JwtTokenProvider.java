package com.beyond.basic.b2_board.common.auth;

import com.beyond.basic.b2_board.author.domain.Author;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value(("${jwt.expirationAt}"))
    private int expirationAt; // JWT 만료 시간 (초 단위)

    @Value(("${jwt.secretKeyAt}"))
    private String secretKeyAt;

    private Key secret_at_key;

    // 스프링 Bean이 만들어지는 시점에 빈이 만들어진 직후에 아래 메서드가 바로 실행
    @PostConstruct
    public void init() {
        secret_at_key = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKeyAt), SignatureAlgorithm.HS512.getJcaName());
    }

    public String createAtToken(Author author) {

        String email = author.getEmail();
        String name = author.getName().toString();
        String role = author.getRole().toString();
        // Claims는 페이로드 (payload)
        Claims claims = Jwts.claims().setSubject(email); // 주된 키값으로 사용되는 subject (id 도 사용 가능) <- 유일한 식별자
        // 주된 키값을 제외한 나머지 사용자 정보는 put 사용하여 key:value 형태로 저장
        claims.put("name", name);
        claims.put("role", role);

        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationAt * 60 * 1000L)) // 만료 시간 설정 (밀리초 단위로 변환)
                // secret key를 통해 마지막 signature를 생성
                .signWith(secret_at_key)
                .compact();
        return token;
    }
}
