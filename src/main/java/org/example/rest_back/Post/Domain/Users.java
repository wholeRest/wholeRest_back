package org.example.rest_back.Post.Domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    // 게시글
    @OneToMany(mappedBy = "users")
    @JsonManagedReference
    private List<Post> posts = new ArrayList<>();

    // 게시글 좋아요
    @OneToMany(mappedBy = "users")
    @JsonManagedReference
    private List<Likes> likes = new ArrayList<>();

    // 댓글
    @OneToMany(mappedBy = "users")
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();
}
