package org.example.rest_back.Post.Service;

import org.example.rest_back.Post.Domain.Comment;
import org.example.rest_back.Post.Dto.CommentDto;
import org.example.rest_back.Post.Repository.CommentRepository;
import org.example.rest_back.Post.Repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // Create, 댓글 생성
    @Transactional
    public Comment registerComment(Comment comment) {
        return commentRepository.save(comment);
    }

    // Read All, 특정 게시글에 해당하는 모든 댓글 조회
    public List<Comment> getAllComments(Long post_id) {
        // 댓글 DB에서 post_id에 해당하는 모든 Entity 조회 가능
        return commentRepository.findByPostId(post_id);
    }

    // Update
    @Transactional
    public Comment updateComment(Long id, CommentDto commentDto){
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

    // Delete, 댓글 삭제
    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }


    // Entity -> Dto
    private CommentDto convertToDto(Comment comment){
        // Entity(post)객체를 Dto(PostDto)객체로 전환
        return CommentDto.builder()
                .comment_id(comment.getComment_id())
                .content(comment.getContent())
                .likes(comment.getLikes())
                .comment_Create_Time(comment.getComment_Create_Time())
                .comment_Update_Time(comment.getComment_Update_Time())
                .post_id(comment.getPost().getPost_id())
                .build();

        // 유저 아이디에 해당하는 코드 작성해야 함
    }
    // Dto -> Entity
    private Comment convertToEntity(CommentDto commentDto){
        // Dto(PostDto)객체를 Entity(post)객체로 전환
        return Comment.builder()
                .comment_id(commentDto.getComment_id())
                .content(commentDto.getContent())
                .likes(commentDto.getLikes())
                .comment_Create_Time(commentDto.getComment_Create_Time())
                .comment_Update_Time(commentDto.getComment_Update_Time())
                .build();

        // 유저 아이디에 해당하는 코드 작성해야 함
    }

}
