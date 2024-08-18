package org.example.rest_back.Post.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.rest_back.Post.Domain.Comment;
import org.example.rest_back.Post.Domain.Post;
import org.example.rest_back.Post.Dto.CommentDto;
import org.example.rest_back.Post.Repository.PostRepository;
import org.example.rest_back.Post.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// 코멘트 컨트롤러
@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    private final PostRepository postRepository;

    @Autowired
    public CommentController(CommentService commentService, PostRepository postRepository) {
        this.commentService = commentService;
        this.postRepository = postRepository;
    }

    // Create, 댓글 생성
    // 유저 아이디 확인하는 로직 작성해야 함
    @PostMapping({"/posts/{post_id}"})
    public ResponseEntity<CommentDto> registerComment(@PathVariable(name = "post_id")Long post_id, @RequestParam(name = "content") String content, HttpServletRequest request){
        Optional<Post> postOptional = postRepository.findById(post_id);
        if (postOptional.isPresent()){
            Comment comment = commentService.registerComment(post_id, content, request);

            return ResponseEntity.ok(commentService.convertToDto(comment));
        }
        else {
            throw new RuntimeException("해당 게시물이 존재하지 않습니다: " + post_id);
        }
    }

    // Read All. 특정 게시물(post_id)에 해당하는 모든 댓글 조회
    @GetMapping({"/posts/{post_id}"})
    public ResponseEntity<List<CommentDto>> getAllComments(@PathVariable(name = "post_id")Long post_id, HttpServletRequest request) {
        List<Comment> comments = commentService.getAllComments(post_id, request);
        List<CommentDto> commentDtos = new ArrayList<>();

        for (Comment comment : comments) {
            commentDtos.add(commentService.convertToDto(comment));
        }

        return ResponseEntity.ok(commentDtos);
    }

    // Update, 댓글 수정
    @PatchMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(name = "id")Long id, @ModelAttribute CommentDto commentDto, HttpServletRequest request){
        try {
            Comment comment = commentService.updateComment(id, commentDto, request);
            return ResponseEntity.ok(commentService.convertToDto(comment));
        }
        catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete, 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        commentService.deleteComment(id, request);
        return ResponseEntity.noContent().build();
    }
}
