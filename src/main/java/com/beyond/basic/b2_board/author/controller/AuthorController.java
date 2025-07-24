package com.beyond.basic.b2_board.author.controller;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dto.*;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.author.service.AuthorService;
import com.beyond.basic.b2_board.common.auth.JwtTokenProvider;
import com.beyond.basic.b2_board.common.auth.SecurityConfig;
import com.beyond.basic.b2_board.common.dto.CommonDto;
import com.beyond.basic.b2_board.common.dto.CommonErrorDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController // Controller + ResponseBody
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorRepository authorRepository;

    // 회원가입
    @PostMapping("/create")
    // Dto에 있는 validation 어노테이션과 controller @Valid 한쌍
    public ResponseEntity<?> save(@Valid @RequestBody AuthorCreateDto authorCreateDto) {
//        try {
//            this.authorService.save(authorCreateDto);
//            return new ResponseEntity<>("OK", HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            // 생성자 매개변수 body 부분의 객체와 header부의 상태코드 기입
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
        // ControllerAdvice가 없었으면 위와 같이 개별적인 예외처리가 필요하나 이제는 전역적인 예외처리가 가능
        this.authorService.save(authorCreateDto);
        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    // 회원 목록조회
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuthorListDto> findAll() {
        return authorService.findAll();
    }

    // 회원 상세조회: id로 조회
    // 서버에서 별도의 try catch를 하지 않으면, 에러 발생 시 500에러 + 스프링의 포맷으로 에러 리턴
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(new CommonDto(authorService.findById(id), HttpStatus.CREATED.value(), "ok"), HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.NOT_FOUND.value(), "Fail such id"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/myInfo")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> myInfo() {
        return new ResponseEntity<>(new CommonDto(authorService.myInfo(), HttpStatus.OK.value(), "마이페이지"), HttpStatus.OK);
    }

    @PatchMapping("/updatepw")
    public ResponseEntity<?> updatePw(@RequestBody AuthorUpdatePwDto authorUpdatePwDto) {
        try {
            authorService.updatePassword(authorUpdatePwDto);
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // 회원 탈퇴(삭제): /author/delete/1
    @DeleteMapping("/delete/{id}")
    // ADMIN 권한이 있는지 authentication 객체에서 쉽게 확인
    // 권한이 없을 경우 filter chain에서 예외가 발생하여 403 에러가 발생
    @PreAuthorize("hasRole('ADMIN')") // 권한이 있는 사용자만 접근 가능 (or로 다른 권한도 추가 가능)
    public String delete(@PathVariable Long id) {
        authorService.delete(id);
        return "OK";
    }

    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody AuthorLoginDto authorLoginDto) {
        Author author = authorService.doLogin(authorLoginDto);
        String token = jwtTokenProvider.createAtToken(author);
        return new ResponseEntity<>
                (new CommonDto(token, HttpStatus.OK.value(), "Token 생성 완료"), HttpStatus.OK);
    }
}
