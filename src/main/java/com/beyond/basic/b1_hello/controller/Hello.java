package com.beyond.basic.b1_hello.controller;

import lombok.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

//@Getter // 클래스 내 모든 변수를 대상으로 getter 생성
@Data   // getter, setter, toString 메서드를 모두 만들어주는 어노테이션
@AllArgsConstructor // 모든 변수를 가지고있는 생성자
@NoArgsConstructor  // 기본 생성자
// 기본 생성자 + getter 로 json parsing이 이루어지므로 필수 요소!!
public class Hello {
    private String name;
    private String email;
//    메서드에 쓰나 객체에 쓰나 똑같음
//    private MultipartFile photo;
}
