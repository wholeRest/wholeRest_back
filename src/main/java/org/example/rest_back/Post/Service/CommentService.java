package org.example.rest_back.Post.Service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.example.rest_back.Post.Domain.Comment;
import org.example.rest_back.Post.Domain.Post;
import org.example.rest_back.Post.Dto.CommentDto;
import org.example.rest_back.Post.Repository.CommentRepository;
import org.example.rest_back.config.jwt.JwtUtils;
import org.example.rest_back.exception.UserNotFoundException;
import org.example.rest_back.user.domain.User;
import org.example.rest_back.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostService postService, JwtUtils jwtUtils, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    // Create, 댓글 생성
    // postService에서 사용자 존재 유무 확인함. 여기서 할 필요 없음
    @Transactional
    public Comment registerComment(Long post_id, String content, HttpServletRequest request) {
        Optional<Post> postOptional = postService.getPostById(post_id, request);
        if (postOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid postId: " + post_id);
        }

        Post post = postOptional.get();

        CommentDto commentDto = CommentDto.builder()
                                    .post(post)
                                    .content(content)
                                    .build();

        return commentRepository.save(convertToEntity(commentDto));
    }

    // Read All, 특정 게시글에 해당하는 모든 댓글 조회
    public List<Comment> getAllComments(Long post_id, HttpServletRequest request) {
        // token 값을 통해 memberId (Stirng) 가져오기
        String token = jwtUtils.getJwtFromHeader(request);
        Claims claims = jwtUtils.getUserInfoFromToken(token);
        String memberId = claims.getSubject();

        // memberId로 user 정보 가져오기
        User user = userRepository.findByMemberId(memberId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }
        else {
            // 댓글 DB에서 post_id에 해당하는 모든 Entity 조회 가능
            return commentRepository.findByPostId(post_id);
        }
    }

    // Update
    @Transactional
    public Comment updateComment(Long id, CommentDto commentDto, HttpServletRequest request){
        // token 값을 통해 memberId (Stirng) 가져오기
        String token = jwtUtils.getJwtFromHeader(request);
        Claims claims = jwtUtils.getUserInfoFromToken(token);
        String memberId = claims.getSubject();

        // memberId로 user 정보 가져오기
        User user = userRepository.findByMemberId(memberId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }
        else {
            // Optional : 값이 있을 수도 있고 없을 수도 있는 객체. null 참조를 피함
            // DB에 저장되어있는 댓글(수정하고자 하는 댓글)에 관한 정보를 commentOptional 객체에 저장
            Optional<Comment> commentOptional = commentRepository.findById(id);

            // commentOptional 객체에 값이 존재하는지 확인
            if (commentOptional.isPresent()) {
                // commentOptional의 값을 반환하여 post 객체에 저장
                Comment comment = commentOptional.get();

                // 객체에 수정사항 반영
                comment.update_comment(commentDto);

                // DB에 수정한 내용 저장
                return commentRepository.save(comment);
            }
            // commentOptional 객체에 값이 존재하지 않으면 예외처리
            else {
                throw new RuntimeException(("Comment not found with id " + id));
            }
        }
    }

    // Delete, 댓글 삭제
    @Transactional
    public void deleteComment(Long id, HttpServletRequest request) {
        // token 값을 통해 memberId (Stirng) 가져오기
        String token = jwtUtils.getJwtFromHeader(request);
        Claims claims = jwtUtils.getUserInfoFromToken(token);
        String memberId = claims.getSubject();

        // memberId로 user 정보 가져오기
        User user = userRepository.findByMemberId(memberId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }
        else {
            commentRepository.deleteById(id);
        }
    }


    // Entity -> Dto
    public CommentDto convertToDto(Comment comment){
        // Entity(post)객체를 Dto(PostDto)객체로 전환
        return CommentDto.builder()
                .comment_id(comment.getComment_id())
                .content(comment.getContent())
                .comment_Create_Time(comment.getComment_Create_Time())
                .comment_Update_Time(comment.getComment_Update_Time())
                .post_id(comment.getPost().getId())
                .build();

        // 유저 아이디에 해당하는 코드 작성해야 함
    }
    // Dto -> Entity
    public Comment convertToEntity(CommentDto commentDto){
        // Dto(PostDto)객체를 Entity(post)객체로 전환
        return Comment.builder()
                .comment_id(commentDto.getComment_id())
                .content(commentDto.getContent())
                .comment_Create_Time(commentDto.getComment_Create_Time())
                .comment_Update_Time(commentDto.getComment_Update_Time())
                .post(commentDto.getPost())
                .build();
    }

}
