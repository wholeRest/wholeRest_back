package org.example.rest_back.Post.Service;

import org.example.rest_back.Post.Domain.Post;
import org.example.rest_back.Post.Dto.PostDto;
import org.example.rest_back.Post.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Create
    // Read All
    // Read Specific Category
    // Read Only One
    // Update
    // Delete

    // Entity -> Dto
    private PostDto convertToDto(Post post){
        // Entity(post)객체를 Dto(PostDto)객체로 전환
        return PostDto.builder()
                .post_id(post.getPost_id())
                .title(post.getTitle())
                .content(post.getContent())
                .views(post.getViews())
                .likes(post.getLikes())
                .category(post.getCategory())
                .post_Create_Time(post.getPost_Create_Time())
                .post_Update_Time(post.getPost_Update_Time())
                .build();

        // 유저 아이디에 해당하는 코드 작성해야 함
    }

    // Dto -> Entity
    private Post convertToEntity(PostDto postDto){
        // Dto(PostDto)객체를 Entity(post)객체로 전환
        return Post.builder()
                .post_id(postDto.getPost_id())
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .views(postDto.getViews())
                .likes(postDto.getLikes())
                .category(postDto.getCategory())
                .post_Create_Time(postDto.getPost_Create_Time())
                .post_Update_Time(postDto.getPost_Update_Time())
                .build();

        // 유저 아이디에 해당하는 코드 작성해야 함
    }
}
