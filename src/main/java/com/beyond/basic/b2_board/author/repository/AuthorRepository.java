package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// SpringDataJpa를 사용하기 위해서는 JpaRepository를 상속해야하고 상속 시 Entity 명과 pk 타입 지정 필요
// JpaRepository를 상속함으로서 JpaRepository의 주요기능(각종CRUD 기능이 사전 구현) 상속
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    // save, findAll, findById 등은 사전에 구현되어 있음
    // 그 외 다른 컬럼으로 조회할 때는 findBy+컬럼명 형식으로 선언만 하면 실행시점에 자동 두현
    Optional<Author> findByEmail(String email);
}
