package org.example.rest_back.Post.Service;

import org.example.rest_back.Post.Domain.Post;
import org.example.rest_back.Post.Dto.PostDto;
import org.example.rest_back.Post.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Create, 게시물 생성
    // @Transactional : 하나의 작업을 하나의 묶음으로 정의, 어떤 작업에서 오류가 발생했을 시
    //                  오류가 발생한 묶음은 버리고 그 이전의 묶음으로 되돌리는 어노테이션
    @Transactional
    public Post registerPost(Post post) {
        return postRepository.save(post);
    }

    // Read All, 모든 게시물 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // Read Specific Category, 특정 카테고리의 게시물들만 조회, 카테고리 값으로 조회
    public List<Post> getPostsByCategory(String category) {
        return postRepository.findByCategory(category);
    }

    // Read Only One, 단 하나의 게시물만 조회, id값으로 조회
    // Optional : 값이 있을 수도 있고 없을 수도 있는 객체. null 참조를 피함
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    // Update, 게시물 수정
    @Transactional
    public Post updatePost(Long id, PostDto postDto){
        // Optional : 값이 있을 수도 있고 없을 수도 있는 객체. null 참조를 피함
        // DB에 저장되어있는 게시물(수정하고자 하는 게시물)에 관한 정보를 postOptional 객체에 저장
        Optional<Post> postOptional = postRepository.findById(id);

        // postOptional 객체에 값이 존재하는지 확인
        if (postOptional.isPresent()) {
            // postOptional의 값을 반환하여 post 객체에 저장
            Post post = postOptional.get();

            // 객체에 수정사항 반영
            post.update_post(postDto);

            // DB에 수정한 내용 저장
            return postRepository.save(post);
        }
        // postOptional 객체에 값이 존재하지 않으면 예외처리
        else {
            throw new RuntimeException(("Post not found with id " + id));
        }
    }

    // Delete, 게시물 삭제
    @Transactional
    public void deletePost(Long id){
        postRepository.deleteById(id);
    }

    // Entity -> Dto
    public PostDto convertToDto(Post post){
        // Entity(post)객체를 Dto(PostDto)객체로 전환
        return PostDto.builder()
                .id(post.getId())
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
    public Post convertToEntity(PostDto postDto){
        // Dto(PostDto)객체를 Entity(post)객체로 전환
        return Post.builder()
                .id(postDto.getId())
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
