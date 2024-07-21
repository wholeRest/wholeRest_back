package org.example.rest_back.Post.Service;

import org.example.rest_back.Post.Domain.Comment;
import org.example.rest_back.Post.Domain.Post;
import org.example.rest_back.Post.Dto.CommentDto;
import org.example.rest_back.Post.Dto.PostDto;
import org.example.rest_back.Post.Repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // Create
    // Read All
    // Update
    // Delete

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
