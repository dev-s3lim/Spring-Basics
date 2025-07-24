package com.beyond.basic.b2_board.common.auth;

import com.beyond.basic.b2_board.common.dto.CommonErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@Slf4j
public class JwtAuthorizationHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error(authException.getMessage()); // 로그에 에러 메시지 기록
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized 상태 코드 설정
        response.setContentType("application/json"); // 응답 타입을 JSON으로 설정
        response.setCharacterEncoding("UTF-8"); // 응답 인코딩 설정

        CommonErrorDto commonErrorDto = new CommonErrorDto(401, "토큰이 없거나 유효하지 않습니다.");
        PrintWriter printWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(commonErrorDto); // CommonErrorDto 객체를 JSON 문자열로 변환
        printWriter.write(body); // JSON 형태로 응답 본문 작성
        printWriter.flush(); // 응답을 클라이언트로 전송
    }
}
