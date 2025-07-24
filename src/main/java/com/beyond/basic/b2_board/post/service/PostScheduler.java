/*
package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostScheduler {
    private final PostRepository postRepository;
    // cron의 각 자리는 초, 분, 시, 일, 월, 요일을 의미
    // * * * * * * 는 매월, 매일, 매시간, 매분, 매초 마다 실행되는것을 의미
    // 0 0 * * * * 는 매월, 매일, 매시간 0분 0초에 실행되는것을 의미
    // 0 0 11 * * * 는 매월, 매일, 11시 0분 0초에 실행되는것을 의미
    // 0 0/1 * * * * 는 매월, 매일, 매시간 0분부터 59분까지 1분마다 실행되는것을 의미
    @Scheduled(cron = "0 0/1 * * * *") // 특정 시간마다 실행되는 자동 프로세스
    public void postSchedule() {
        log.info("PostScheduler has begun...");
        List<Post> postList = postRepository.findByAppointment("Y");
        LocalDateTime now = LocalDateTime.now();

        for (Post p : postList) {
            if (p.getAppointmentTime().isBefore(now)){
                p.updateAppointment("N");
            }
        }
        log.info("PostScheduler is done...");
    }
}
 */