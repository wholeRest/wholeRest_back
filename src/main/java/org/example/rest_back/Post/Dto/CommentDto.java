package org.example.rest_back.Post.Dto;

import lombok.*;
import org.example.rest_back.Post.Domain.Post;
import org.example.rest_back.user.domain.User;

import java.time.LocalDateTime;

// 계층간 데이터 교환을 위한 클래스
@Getter
@Setter
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
public class CommentDto {
    private Long comment_id;
    private String content;
    private LocalDateTime comment_Create_Time;
    private LocalDateTime comment_Update_Time;
    private Post post;
    private Long post_id;
    private Long user_id;
    private User user;
}
