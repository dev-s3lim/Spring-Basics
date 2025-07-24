package com.beyond.basic.b2_board.author.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.domain.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data   // dto 계층은 데이터의 안정성이 엔티티만큼 중요하지는 않으므로 setter도 일반적으로 추가
@Builder
public class AuthorCreateDto {
    @NotEmpty(message = "이름을 입력해 주세요")
    private String name;
    @NotEmpty(message = "이메일을 입력해 주세요")
    private String email;
    @NotEmpty(message = "비밀번호를 입력해 주세요")
    @Size(min = 8, message = "비밀번호가 너무 짧습니다.")
    private String password;
    private String profileImageUrl; // 프로필 이미지 URL
    // 문자열로 값이 넘어오면 Role에 값으로 매핑

    public Author authorToEntity() {
        // 빌더 패턴은 매개변수의 갯수와 매개변수의 순서에 상관없이 객체 생성 가능
        return Author.builder()
                .name(this.name)
                .password(this.password)
                .email(this.email)
                .role(Role.USER) // 기본값은 USER로 설정
                .profileImageUrl(this.profileImageUrl)
                .build();
    }
}
