package com.beyond.basic.b3_servlet;

import com.beyond.basic.b1_hello.controller.Hello;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

// servlet은 사용자으 req를 쉽게 처리하고, 사용자에게 res를 쉽게 조립해주는 기술
// servlet에서는 url 맵핑을 메서드 단위가 아닌, 클래스 단위로 지정
@WebServlet ("/servlet/get")
public class ServletRestGet extends HttpServlet {

    @Override
    // 사용자의 요청이 req에 담겨서 오면, 이 메서드가 실행된다.
    // Controller에는 HttpServletRequest와 HttpServletResponse가 이미 주입되어 있다.
    // HttpServletRequest와 HttpServletResponse는 부모<->자식 관계다.
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Hello hello = new Hello();
        hello.setName("홍길동");
        hello.setEmail("hong@naver.com");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(hello);

        PrintWriter printWriter = resp.getWriter();
        printWriter.print(body);
        printWriter.flush();
    }

    /// 1. 401, 403에 일관성 없음
    /// 2. ErrorDto 사용하지 않고 서버에 로그도 없음
    /// 토큰 관련 에러 상황 :
        /// 1. 있어야 하는 토큰이 없는 경우 (401 Unauthorized)
        /// 2. 토큰이 있지만, 조작된 경우 (403 Forbidden)
        /// 3. 토큰이 있지만, 만료된 경우 (401 Unauthorized)
}
