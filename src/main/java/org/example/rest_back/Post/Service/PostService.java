package org.example.rest_back.Post.Service;

import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.example.rest_back.Post.Domain.Post;
import org.example.rest_back.Post.Dto.PostDto;
import org.example.rest_back.Post.Repository.PostRepository;
import org.example.rest_back.config.jwt.JwtUtils;
import org.example.rest_back.user.domain.User;
import org.example.rest_back.exception.UserNotFoundException;
import org.example.rest_back.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final ImageService imageService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;


    @Autowired
    public PostService(PostRepository postRepository, ImageService imageService, JwtUtils jwtUtils, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.imageService = imageService;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    // Create, 게시물 생성
    // @Transactional : 하나의 작업을 하나의 묶음으로 정의, 어떤 작업에서 오류가 발생했을 시
    //                  오류가 발생한 묶음은 버리고 그 이전의 묶음으로 되돌리는 어노테이션
    @Transactional
    public Post registerPost(PostDto postDto, HttpServletRequest request) {
        // token 값을 통해 memberId (Stirng) 가져오기
        String token = jwtUtils.getJwtFromHeader(request);
        Claims claims = jwtUtils.getUserInfoFromToken(token);
        String memberId = claims.getSubject();

        // memberId로 user 정보 가져오기
        User user = userRepository.findByMemberId(memberId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }

//        Post post = Post.builder()
//                .title(postDto.getTitle())
//                .content(postDto.getContent())
//                .imgURLs(postDto.getImgURLs())
//                .category(postDto.getCategory())
//                .user(user)
//                .build();
        Post post = convertToEntity(postDto, user);

        return postRepository.save(post);
    }

    // Read All, 모든 게시물 조회
    public List<Post> getAllPosts(HttpServletRequest request) {

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
            return postRepository.findAll();
        }
    }

    // Read Specific Category, 특정 카테고리의 게시물들만 조회, 카테고리 값으로 조회
    public List<Post> getPostsByCategory(String category, HttpServletRequest request) {
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
            return postRepository.findByCategory(category);
        }
    }

    // Read Only One, 단 하나의 게시물만 조회, id값으로 조회
    // Optional : 값이 있을 수도 있고 없을 수도 있는 객체. null 참조를 피함
    public Optional<Post> getPostById(Long id, HttpServletRequest request) {
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
            Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post not found with id " + id));
            post.addViews(1); // 사용자가 게시물 조회 시 조회수 증가
            return postRepository.findById(id);
        }
    }

    // Update, 게시물 수정
    @Transactional
    public Post updatePost(Long id, PostDto postDto, HttpServletRequest request){
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
    }

    // Delete, 게시물 삭제
    @Transactional
    public void deletePost(Long id, HttpServletRequest request){
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
            postRepository.deleteById(id);
        }
    }

    // Delete image, 게시물 내의 이미지 삭제
    public void deleteImageFromPost(Long id, String imageUrl, HttpServletRequest request) {
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
            Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post not found with id " + id));
            List<String> imgUrls = post.getImgURLs();

            if (imgUrls.contains(imageUrl)) {
                imgUrls.remove(imageUrl);
                Post newPost = Post.builder()
                        .title(post.getTitle())
                        .id(post.getId())
                        .content(post.getContent())
                        .category(post.getCategory())
                        .likes_count(post.getLikes_count())
                        .views(post.getViews())
                        .imgURLs(imgUrls)
                        .build();

                postRepository.save(newPost);
                imageService.deleteFileInS3(imageUrl);
            }
        }
    }

    // Entity -> Dto
    public PostDto convertToDto(Post post){
        // Entity(post)객체를 Dto(PostDto)객체로 전환
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .views(post.getViews())
                .category(post.getCategory())
                .likes_count(post.getLikes_count())
                .post_Create_Time(post.getPost_Create_Time())
                .post_Update_Time(post.getPost_Update_Time())
                .imgURLs(post.getImgURLs())
                .build();
    }

    // Dto -> Entity
    public Post convertToEntity(PostDto postDto, User user){
        // Dto(PostDto)객체를 Entity(post)객체로 전환
        return Post.builder()
                .id(postDto.getId())
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .views(postDto.getViews())
                .category(postDto.getCategory())
                .likes_count(postDto.getLikes_count())
                .imgURLs(postDto.getImgURLs())
                .user(user)
                .build();
    }
}
