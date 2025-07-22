package com.beyond.basic.b2_board.common.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// logvack 객체 만드는 방법2.
@Slf4j
@RestController
public class LogController {
//    // logback 객체 만드는 법
//    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @GetMapping("/log/test")
    public String logTest() {
        // 기존의 system print는 spring에서 잘 사용하지 않음
        // 이유 1) 성능이 떨어짐, 2) 로그 분류작업 불가
        System.out.println("Hello world");

        // 가장많이 사용되는 로그 라이브러리: logback
        // 로그 레벨(프로젝트차원에 설정): trace < debug < info < error
//        logger.trace("trace 로그입니다.");
//        logger.debug("debug 로그입니다.");
//        logger.info("info 로그입니다.");
//        logger.error("error 로그입니다.");

        // Slf4j 어노테이션 선언 시 log라는 변수로 logback 객체 사용 가능
        log.info("Slf4j 테스트");
        try {
            log.info("에러테스트 시작");
            throw new RuntimeException("에러 테스트");
        } catch (RuntimeException e) {
            log.error("에러메시지: ", e);
//            e.printStackTrace(); // 성능 문제로 logback쓰는게 더 좋음
        }
        return "OK";
    }
}
