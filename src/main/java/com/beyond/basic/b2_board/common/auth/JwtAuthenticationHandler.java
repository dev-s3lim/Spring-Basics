package com.beyond.basic.b2_board.common.auth;

import com.beyond.basic.b2_board.common.dto.CommonErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

// 401에러인 경우 잡아내겠다.
@Component
@Slf4j
public class JwtAuthenticationHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error(accessDeniedException.getMessage()); // 로그에 에러 메시지 기록
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized 상태 코드 설정
        response.setContentType("application/json"); // 응답 타입을 JSON으로 설정
        response.setCharacterEncoding("UTF-8"); // 응답 인코딩 설정

        CommonErrorDto commonErrorDto = new CommonErrorDto(401, "권한이 없습니다.");
        PrintWriter printWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(commonErrorDto); // CommonErrorDto 객체를 JSON 문자열로 변환
        printWriter.write(body); // JSON 형태로 응답 본문 작성
        printWriter.flush(); // 응답을 클라이언트로 전송
    }
}
