package org.example.rest_back.Post.Domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.rest_back.Post.Dto.PostDto;
import org.example.rest_back.user.domain.User;
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
    private Long id;

     //유저아이디값을 외래키로 가짐 (String)
     @ManyToOne
     @JsonBackReference
     @JoinColumn(name = "user_id")
     private User user;

    // 게시물 제목
    private String title;

    // 게시물 내용
    private String content;

    // 게시물 조회수
    private int views;

    // 게시물 좋아요수
    private int likes_count;

    // 게시물의 카테고리
    private String category;

    // S3에 저장된 이미지 주소
    @ElementCollection
    private List<String> imgURLs;

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
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    // 게시글 좋아요
    @OneToMany(mappedBy = "post")
    @JsonManagedReference
    private List<Likes> likes = new ArrayList<>();

    // Entity가 DB에 insert되기 전에 자동으로 호출됨
    // DB에 게시글 생성 시간 자동으로 삽입 (생성시간, 수정시간 모두 동일하게 적용)
    @PrePersist
    protected void onCreate() {
        this.post_Create_Time = LocalDateTime.now();
        this.post_Update_Time = LocalDateTime.now();
    }

    // Entity가 DB에 Update되기 전에 자동으로 호출됨
    // DB에 게시글 수정 시간 자동으로 삽입 (수정시간에만 적용)
    // Entity 변경이 아닌 실제 DB에 있는 데이터가 변경되는 경우 호출됨
    // 즉, 실제 SQL 업데이트 문이 나가야 해당 콜백이 호출됨
    @PreUpdate
    protected void onUpdate() {
        this.post_Update_Time = LocalDateTime.now();
    }

    // Setter보다 보안성이 강한 Entity 수정 방법
    public void update_post(PostDto postDto){
        if (postDto.getContent() != null) {
            this.content = postDto.getContent();
        }
        if (postDto.getTitle() != null) {
            this.title = postDto.getTitle();
        }
    }

    public void addLike(int n){
        this.likes_count += n;
    }

    public void deleteLike(int n){
        this.likes_count -= n;
    }

    public void addViews(int n){
        this.views += n;
    }
}
