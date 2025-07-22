package com.beyond.basic.b2_board.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor // 또는 @Autowired
// PreAuthorize 어노테이션을 사용하기 위해 설정
@EnableMethodSecurity // filterchain -> @EnableMethodSecurity
public class SecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    // 내가 만든 객체(클래스)는 @Component 붙이면 됨
    // 스프링이 제공하는 객체는 @Configuration 붙이고 @Bean 붙여야함
    // @Bean은 메소드 위에 붙여 return 되는 객체를 싱글톤 객체로 생성. Component는 클래스 위에 붙임
    // filter 계층에서 filter logic을 customize 한다. (로그인 메서드는 필터에서 제외될 수 있도록)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // cors : 특정 도메인에 대한 허용 정책, postman는 cors 정책이 없어서 허용됨
                .cors(c -> c.configurationSource(corsConfiguration()))
                // csrf: CSRF(Cross-Site Request Forgery) 공격 방지 기능 -> 보안 공격 중 하나로서 타사이트의 쿠키 값을 꺼내서 탈취하는 공격
                // 세션 기반 로그인 (서버에서 화면을 만들어 주는 경우 mvc, ssr)에서는 csrf 별도 설정하는 것이 일반적
                // 토큰 기반 로그인 (rest api서버, csr)에서는 csrf 설정을 하지 않는 것이 일반적
                .csrf(AbstractHttpConfigurer::disable)
                // htttpBasic: email, pw를 인코딩하여 인증하는 방식 -> 사내 시스템 같이 간단하게 내부에서만 사용하는 경우에 적합
                .httpBasic(AbstractHttpConfigurer::disable) // httpBasic는 보안이 취약하기 때문에 disable
                // 세션 로그인 방식 비활성화 (Stateless -> 토큰 기반 로그인 방식으로 가겠다)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 이 단계에서 token을 검증하고 검증을 통해 Authentication 객체를 생성한다.
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class) // [필터] 못 만든다.
                // 예외 api 정책 설정 (특정 url에 대해서는 인증 없이 접근 가능하도록 설정)
                // authenticated -> 인증된 사용자만 접근 가능 (예외를 제외한 나머지 모든 요청에 대해 Authentication 객체가 생성되기를 요구)
                .authorizeHttpRequests(a -> a.requestMatchers( // [예외처리] 통과
                        "/author/create",
                                "/author/doLogin").permitAll().anyRequest().authenticated())
                .build();
    }

    private CorsConfigurationSource corsConfiguration(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("*")); // 모든 HTTP(get, post 등) 메서드 허용
        configuration.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더요소(Authorization 등) 허용
        configuration.setAllowCredentials(true); // 자격 증명 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); //모든 url패턴에 대해 cors설정 적용
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}