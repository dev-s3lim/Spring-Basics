package com.beyond.basic.b2_board.author.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthorUpdatePwDto {
    private String email;
    private String password;
}
