package com.beyond.basic.b2_board.post.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class PostCreateDto {
    private String title;
    private String category;
    private String contents;
    // private Long authorId;
    private String delYn;
    @Builder.Default
    private String appointment = "N";
    // 시간 정보는 직접 LocalDateTime으로 받지 않고 문자열로 받음
    private String appointmentTime;

    public Post toEntity(Author author, LocalDateTime appointmentTime) {
        return Post.builder()
                .title(this.title)
                .category(this.category)
                .contents(this.contents)
                .author(author)
                .appointment(this.appointment)
                .appointmentTime(appointmentTime)
                .delYn("N")
                .build();
    }
}
