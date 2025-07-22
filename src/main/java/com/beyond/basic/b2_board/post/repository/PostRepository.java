package com.beyond.basic.b2_board.post.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
//    select * from post where author_id = ? and title = ?
//    List<Post> findByAuthorIdAndTitle(Long author, String title);

//    select * from post where author_id = ? and title = ? order by createdTime desc;
//    List<Post> findByAuthorIdAndTitleOrderByCreatedTimeDesc(Long author, String title);
//    List<Post> findByAuthorId(Long authorId);
//    List<Post> findByAuthor(Author author);

    // jpql을 사용한 일반 inner join
    // jpql는 기본적으로 lazy로딩 지향하므로 inner join으로 filtering은 하되 post 객체만 조회 -> N+1 문제 여전히 발생
    // raw 쿼리: select p.* from post p inner join author a on a.id=p.author_id;
    @Query("select p from Post p inner join p.author")
    List<Post> findAllJoin();

    // jpql을 사용한 fetch inner join
    // jpql시 post 뿐만 아니라 author 객체까지 한 번에 조립하여 조회 -> N+1 문제 해결
    @Query("select p from Post p inner join fetch p.author")
    List<Post> findAllFetchJoin();

    // paging 처리 + delYn 적용
    // import org.springframework.data.domain.Pageable;
    // Page 객체 안에 List<Post>, 전체 페이지 수 등의 정보도 포함 되어있음
    // Pageable 객체 안에는 페이지 size, 페이지 번호, 정렬 기준 등 포함
    Page<Post> findAll(Pageable pageable);
    Page<Post> findAllByDelYn(Pageable pageable, String delYn);

}

