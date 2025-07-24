package com.beyond.basic;

import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

// ComponentScan은 Application 파일을 포함한 경로 하위의 요소들만 scan 가능
@SpringBootApplication
// 주로 웹서블릿 기반의 구성요소(@WebServlet)를 스캔하고 자동으로 Bean (Singleton)으로 등록하기 위해 사용
@ServletComponentScan
// 스케줄러를 사용하기 위해서 추가
@EnableScheduling
public class BasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicApplication.class, args);
    }

}
