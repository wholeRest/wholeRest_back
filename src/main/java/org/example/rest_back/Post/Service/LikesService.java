package org.example.rest_back.Post.Service;

import jakarta.persistence.EntityNotFoundException;
import org.example.rest_back.Post.Domain.Likes;
import org.example.rest_back.Post.Domain.Post;
import org.example.rest_back.Post.Domain.Users;
import org.example.rest_back.Post.Dto.LikesDto;
import org.example.rest_back.Post.Repository.LikesRepository;
import org.example.rest_back.Post.Repository.PostRepository;
import org.example.rest_back.Post.Repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class LikesService {
    private final LikesRepository likesRepository;
    private final PostRepository postRepository;
    private final UsersRepository usersRepository;

    public LikesService(LikesRepository likesRepository, PostRepository postRepository, UsersRepository usersRepository) {
        this.likesRepository = likesRepository;
        this.postRepository = postRepository;
        this.usersRepository = usersRepository;
    }

    // 게시물 좋아요
    @Transactional
    public Likes insertLike(LikesDto likesDto) throws Exception {
        // Optional 객체가 아닌 Users 객체에 데이터를 저장할 수 있음
        Users users = usersRepository.findById(likesDto.getUser_id()).orElseThrow(() -> new EntityNotFoundException("User not found with id " + likesDto.getUser_id()));
        Post post = postRepository.findById(likesDto.getPost_id()).orElseThrow(() -> new EntityNotFoundException("User not found with id " + likesDto.getPost_id()));

        if (likesRepository.findByUsersAndPost(users, post).isPresent()) {
            throw new Exception();
        }

        post.addLike(1);

        Likes likes = Likes.builder().post(post).users(users).build();
        postRepository.save(post); //변경된 좋아요 수를 DB에 반영
        return likesRepository.save(likes);
    }

    // 게시물 좋아요 취소
    @Transactional
    public void deleteLike(LikesDto likesDto) {
        Users users = usersRepository.findById(likesDto.getUser_id()).orElseThrow(() -> new EntityNotFoundException("User not found with id " + likesDto.getUser_id()));
        Post post = postRepository.findById(likesDto.getPost_id()).orElseThrow(() -> new EntityNotFoundException("User not found with id " + likesDto.getPost_id()));

        Likes likes = likesRepository.findByUsersAndPost(users, post).orElseThrow(() -> new EntityNotFoundException("User not found Like id "));
        likesRepository.delete(likes);
        post.deleteLike(1);
        postRepository.save(post); //변경된 좋아요 수를 DB에 반영
    }

    // Entity -> Dto
    public LikesDto convertToDto(Likes likes){
        return LikesDto.builder()
                .id(likes.getId())
                .user_id(likes.getUsers().getId())
                .post_id(likes.getPost().getId())
                .build();
    }

    // Dto -> Entity
    public Likes convertToEntity(LikesDto likesDto){
        return Likes.builder()
                .id(likesDto.getId())
                .users(likesDto.getUsers())
                .post(likesDto.getPost())
                .build();
    }

}
