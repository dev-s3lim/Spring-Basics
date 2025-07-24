package com.beyond.basic.b2_board.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/// 해당 DTO의 경우에는 toEntity 메서드가 필요하지 않음
/// 이유 : 검색 조건을 담는 DTO이기 때문
/// 검색 조건을 담는 DTO는 일반적으로 엔티티로 변환할 필요가 없음
/// toEntity를 사용해야 하는 경우 : 새로운 엔티티를 생성할 때
/// 예를 들어, 게시물 작성 시에는 PostCreateDto와 같은 DTO가 필요하며, 이 경우에는 toEntity 메서드가 필요함
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostSearchDto {
    String title; // 제목
    String category; // 카테고리
    String contents; // 내용
}
