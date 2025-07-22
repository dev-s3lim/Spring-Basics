package com.beyond.basic.b2_board.post.domain;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString

public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(length = 3000)
    private String contents;
    @Builder.Default
    private String delYn = "n";

    // FK 설정 시 ManyToOne과 같은 관계성은 필수
    // ManyToOne에서는 default가 fetch.EAGER(즉시로딩) 설정: author 객체를 사용하지 않아도 author 테이블로 쿼리 발생
    // 일반적으로 LAZY(지연로딩) 설정: author 객체를 사용하지 않는 한 author 객체로 쿼리 발생 X
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id") // fk 관계성
    private Author author;
}