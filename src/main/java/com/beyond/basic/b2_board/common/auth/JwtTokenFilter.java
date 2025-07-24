package com.beyond.basic.b2_board.common.auth;

import io.jsonwebtoken.*;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class JwtTokenFilter extends GenericFilter  {
    @Value("${jwt.secretKeyAt}")
    private String secretKey;
    @Override
    // 원본
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
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
            // 토큰 조작 시 여기서 에러 터짐
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

            List<GrantedAuthority> authorities = new ArrayList<>(); // 권한은 List 형태로 관리

            // authentication 객체를 만들 때 권한은 ROLE_ 라는 키워드를 붙여서 만들어 주는 것이 추후 문제 발생X
            authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
            Authentication authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
            // payload가 아닌 authentication 객체를 만들어서 SecurityContextHolder에 저장한다.
            SecurityContextHolder.getContext().setAuthentication(authentication);// 다음 필터로 넘어간다.
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        chain.doFilter(request, response);

    }

    // 사용자 -> Request(header + body) -> HttpServletRequest (Controller의 근간이 되는 객체) -> Spring
    // Controller는 사용자가 빈번하게 사용하는 정보들만 json 형태로 변환하여 응답한다.
    // ObjectMapper는 객체를 json 형태로 변환해주는 객체
    // HttpServletRequest는 사용자가 요청한 정보들을 담고 있는 객체
    // HttpServletResponse는 사용자가 응답받을 정보들을 담고 있는 객체
}
