package org.example.rest_back.Post.Domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// DB 테이블과 매칭될 클래스
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
// @NoArgsConstructor : 파라미터가 없는 디폴트 생성자를 자동으로 생성
// @AllArgsConstructor : 모든 필드 값을 파라미터로 받는 생성자 자동으로 생성
@Builder
// 어떤 파라미터가 전달되었는지 정확하게 파악하기 위해, 필요한 값만 넣기 위해 builder 패턴 사용
// 사용 방법
// Post post = new post.builder()
//              .Title("안녕하세요")
//              .Content("저는 홍길동입니다")
//              .build();
public class Post {

    // 기본키
    // 각 게시물의 고유한 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long post_id;

    //@ManyToOne
    //JoinColumn(name = "user_id")
    //private User user_id;

    // 게시물 제목
    private String title;

    // 게시물 내용
    private String content;

    // 게시물 조회수
    private int views;

    //게시물 좋아요수
    private int likes;

    // 게시물의 카테고리
    private String category;

    // 게시물 생성 시간
    // @CreationTimestamp : insert 쿼리 발생 시, 즉 데이터가 생성된 시점에 대한 생성 시간 저장
    @CreationTimestamp
    private LocalDateTime post_Create_Time;

    // 게시물 수정 시간
    // @UpdateTimestamp : update 쿼리 발생 시, 즉 수정이 발생할 때마다 마지막 수정 시간을 업데이트
    @UpdateTimestamp
    private LocalDateTime post_Update_Time;

    // 코멘트 필드. 하나의 게시물에 여러 댓글이 달릴 수 있음 (일대다 관계)
    // Comment 엔터티에서 Post 엔터티와의 관계를 나타내는 필드 존재 (private Post post)
    // 따라서 mappedBy에서는 Comment엔터티에서 Post엔터티를 참조하는 post필드를 참조해야 함.
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();
}
