package org.example.rest_back.Post.Service;

import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.example.rest_back.Post.Domain.Likes;
import org.example.rest_back.Post.Domain.Post;
import org.example.rest_back.Post.Dto.LikesDto;
import org.example.rest_back.Post.Repository.LikesRepository;
import org.example.rest_back.Post.Repository.PostRepository;
import org.example.rest_back.config.jwt.JwtUtils;
<<<<<<< HEAD
import org.example.rest_back.exception.UserNotFoundException;
import org.example.rest_back.user.domain.User;
=======
import org.example.rest_back.user.domain.User;
import org.example.rest_back.exception.UserNotFoundException;
>>>>>>> 6eb92bc6029ac91d2c69cee7e069683c34c8354a
import org.example.rest_back.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class LikesService {
    private final LikesRepository likesRepository;
    private final PostRepository postRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public LikesService(LikesRepository likesRepository, PostRepository postRepository, JwtUtils jwtUtils, UserRepository userRepository) {
        this.likesRepository = likesRepository;
        this.postRepository = postRepository;

        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    // 게시물 좋아요
    @Transactional
    public Likes insertLike(LikesDto likesDto, HttpServletRequest request) throws Exception {
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
            Post post = postRepository.findById(likesDto.getPost_id()).orElseThrow(() -> new EntityNotFoundException("Post not found with id " + likesDto.getPost_id()));
            if (likesRepository.findByUserAndPost(user, post).isPresent()) {
                throw new Exception();
            }

            post.addLike(1);

            Likes likes = Likes.builder().post(post).user(user).build();
            postRepository.save(post); //변경된 좋아요 수를 DB에 반영
            return likesRepository.save(likes);
        }
    }

    // 게시물 좋아요 취소
    @Transactional
    public void deleteLike(LikesDto likesDto, HttpServletRequest request) {
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
            Post post = postRepository.findById(likesDto.getPost_id()).orElseThrow(() -> new EntityNotFoundException("User not found with id " + likesDto.getPost_id()));

            Likes likes = likesRepository.findByUserAndPost(user, post).orElseThrow(() -> new EntityNotFoundException("User not found Like id "));
            likesRepository.delete(likes);
            post.deleteLike(1);
            postRepository.save(post); //변경된 좋아요 수를 DB에 반영
        }
    }

    // Entity -> Dto
    public LikesDto convertToDto(Likes likes){
        return LikesDto.builder()
<<<<<<< HEAD
=======
                .id(likes.getId())
                .user_id(likes.getUser().getUser_id())
>>>>>>> 6eb92bc6029ac91d2c69cee7e069683c34c8354a
                .post_id(likes.getPost().getId())
                .build();
    }

    // Dto -> Entity
    public Likes convertToEntity(LikesDto likesDto){
        return Likes.builder()
<<<<<<< HEAD
=======
                .id(likesDto.getId())
                .user(likesDto.getUser())
>>>>>>> 6eb92bc6029ac91d2c69cee7e069683c34c8354a
                .post(likesDto.getPost())
                .build();
    }

}
