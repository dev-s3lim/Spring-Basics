package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dto.PostCreateDto;
import com.beyond.basic.b2_board.post.dto.PostDetailDto;
import com.beyond.basic.b2_board.post.dto.PostListDto;
import com.beyond.basic.b2_board.post.dto.PostSearchDto;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    public void save(PostCreateDto dto){
        // authorId가 실제 있는지 없는지 검증 필요
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); //claims의 subject : email
        System.out.println(email);
        /*
        Author author = authorRepository.findById(dto.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("없는 ID입니다."));
        postRepository.save(dto.toEntity(author));
         */
        Author author = authorRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 ID입니다."));
        LocalDateTime appointmentTime = null;
        if (dto.getAppointment().equals("Y")){
            if (dto.getAppointmentTime() == null || dto.getAppointmentTime().isEmpty()) {
                throw new IllegalArgumentException("시간정보가 비어 있습니다.");
            }
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            appointmentTime = LocalDateTime.parse(dto.getAppointmentTime(), dateTimeFormatter);
        }
        postRepository.save(dto.toEntity(author, appointmentTime));
    }

    public PostDetailDto findById(Long id){
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("없는 ID입니다."));
        /// 엔티티간의 관계성 설정을 하지 않았을 때
//        Author author = authorRepository.findById(post.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("없는 회원번호입니다."));
//        return PostDetailDto.fromEntity(post, author);

        /// 엔티티 간 관계성 설정을 하여 Author 객체를 쉽게 조회하는 경우
        return PostDetailDto.fromEntity(post);
    }

    public List<PostListDto> findAllNoPaging() {
        List<Post> postList = postRepository.findAllWithAuthor(); // fetch join 포함
        return postList.stream()
                .map(PostListDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<PostListDto> findAll(Pageable pageable, PostSearchDto dto){
        /// testcase: author: 3, post: 3
//        List<Post> postList = postRepository.findAll();
//          Hibernate: select p1_0.id,p1_0.author_id,p1_0.contents,p1_0.created_time,p1_0.title,p1_0.updated_time from post p1_0
//          Hibernate: select a1_0.id,a1_0.created_time,a1_0.email,a1_0.name,a1_0.password,a1_0.role,a1_0.updated_time from author a1_0 where a1_0.id=?
//          Hibernate: select a1_0.id,a1_0.created_time,a1_0.email,a1_0.name,a1_0.password,a1_0.role,a1_0.updated_time from author a1_0 where a1_0.id=?
//          Hibernate: select a1_0.id,a1_0.created_time,a1_0.email,a1_0.name,a1_0.password,a1_0.role,a1_0.updated_time from author a1_0 where a1_0.id=?
        /// 총 4개로 N+1 문제 발생

//        List<Post> postList = postRepository.findAllJoin();
//        Hibernate: select p1_0.id,p1_0.author_id,p1_0.contents,p1_0.created_time,p1_0.title,p1_0.updated_time from post p1_0 join author a1_0 on a1_0.id=p1_0.author_id
//        Hibernate: select a1_0.id,a1_0.created_time,a1_0.email,a1_0.name,a1_0.password,a1_0.role,a1_0.updated_time from author a1_0 where a1_0.id=?
//        Hibernate: select a1_0.id,a1_0.created_time,a1_0.email,a1_0.name,a1_0.password,a1_0.role,a1_0.updated_time from author a1_0 where a1_0.id=?
//        Hibernate: select a1_0.id,a1_0.created_time,a1_0.email,a1_0.name,a1_0.password,a1_0.role,a1_0.updated_time from author a1_0 where a1_0.id=?
        /// 총 4개로 N+1 문제 발생

//        List<Post> postList = postRepository.findAllFetchJoin();
//        Hibernate: select p1_0.id,a1_0.id,a1_0.created_time,a1_0.email,a1_0.name,a1_0.password,a1_0.role,a1_0.updated_time,p1_0.contents,p1_0.created_time,p1_0.title,p1_0.updated_time from post p1_0 join author a1_0 on a1_0.id=p1_0.author_id
        /// 총 1개로 N+1 문제 해결

        // postlist를 조회할 때 참조관계에 있는 author까지 조회하게 되므로 N(author쿼리)+1(post쿼리) 문제 발생
        // jpa는 기본 방향성이 fetch lazy 이므로, 참조하는 시점에 쿼리를 내보내게 되어 직접 조인문을 만들어 주지 않고 N+1문제 발생


        // 페이지 처리 findAll 호출
//        Page<Post> postList = postRepository.findAll(pageable);

        // 검색을 위해 Specification을 어노테이션 제공 중
        // Specification 객체는 복잡한 쿼리를 명세를 이용하여 정의하는 방식으로 쿼리를 쉽게 생성
        Specification<Post> specification = new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                // Root 객체 = Post 엔티티의 속성에 접근하기 위한 객체
                // CriteriaBuilder 객체 = 쿼리 생성하기 위한 객체
                List<Predicate> predicateList = new ArrayList<>();
                // 1. delYn이 N인 것만 조회
                // 2. appointment가 N인 것만 조회
                // 아래 두개는 무조건 깔려들어가는 조건
                predicateList.add(criteriaBuilder.equal(root.get("delYn"), "N"));
                predicateList.add(criteriaBuilder.equal(root.get("appointment"), "N"));
                if (dto.getTitle() != null){
                    predicateList.add(criteriaBuilder.like(root.get("title"), "%" + dto.getTitle() + "%"));
                }
                if (dto.getCategory() != null){
                    predicateList.add(criteriaBuilder.equal(root.get("category"), dto.getCategory()));
                }
                if (dto.getContents() != null){
                    predicateList.add(criteriaBuilder.like(root.get("contents"), "%" + dto.getContents() + "%"));
                }
                // SELECT * FROM post WHERE delYn = 'N' AND title LIKE '%검색어%' AND category = '카테고리명';
                Predicate[] predicateArr = new Predicate[predicateList.size()];
                for (int i = 0; i < predicateList.size(); i++) {
                    predicateArr[i] = predicateList.get(i);
                }
                // 위의 검색 조건들을 하나의 (한줄의 쿼리로) Predicate 객체로 만들어서 return
                return criteriaBuilder.and(predicateArr);
            }
        };

        Page<Post> postList = postRepository.findAll(specification, pageable);
//        return postList.stream().map(a -> PostListDto.fromEntity(a)).collect(Collectors.toList());
        return postList.map(a -> PostListDto.fromEntity(a));
    }
}
