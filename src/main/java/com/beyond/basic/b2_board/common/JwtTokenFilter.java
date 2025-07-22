package com.beyond.basic.b2_board.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtTokenFilter extends GenericFilter  {
    @Value("${jwt.secretKeyAt}")
    private String secretKey;
    @Override
    // 원본
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 토큰을 꺼내야 한다.
        HttpServletRequest req = (HttpServletRequest) request; // 현재 req에 사용자 정보 담김
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken == null) {
            // 토큰이 없는 경우, filterchain으로 다시 돌아가라
            chain.doFilter(request, response);
            return;
            // 토큰이 있는 경우, 검증 후 Authentication 객체 생성한다.
        }

        String token = bearerToken.substring(7); // "Bearer " 부분을 제외한 토큰만 추출
        // token 검증 및 claims 추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        /*
        claims.getSubject(); // 이메일
        claims.get("name"); // 이름
        claims.get("role"); // 권한
         */

        List<GrantedAuthority> authorities = new ArrayList<>();

        // authentication 객체를 만들 때 권한은 ROLE_ 라는 키워드를 붙여서 만들어 주는 것이 추후 문제 발생X
        authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
        Authentication authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response); // 다음 필터로 넘어간다.
    }
}
