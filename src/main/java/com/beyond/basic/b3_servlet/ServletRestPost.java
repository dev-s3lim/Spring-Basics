package com.beyond.basic.b3_servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet ("/servlet/post")
public class ServletRestPost extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //url 인코딩 방식으로 데이터 전송
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        System.out.println(name);
        System.out.println(email);

        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8"); //한글 setting

        PrintWriter printWriter = resp.getWriter();
        printWriter.print("ok");
        printWriter.flush();
    }
}
