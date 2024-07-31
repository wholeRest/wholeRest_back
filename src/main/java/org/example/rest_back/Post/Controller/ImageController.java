package org.example.rest_back.Post.Controller;

import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.example.rest_back.Post.Domain.Likes;
import org.example.rest_back.Post.Domain.Post;
import org.example.rest_back.Post.Service.ImageService;
import org.example.rest_back.Post.Service.PostService;
import org.example.rest_back.config.jwt.JwtUtils;
import org.example.rest_back.user.domain.User;
import org.example.rest_back.user.domain.exception.UserNotFoundException;
import org.example.rest_back.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;
    private final PostService postService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Autowired
    public ImageController(ImageService imageService, PostService postService, JwtUtils jwtUtils, UserRepository userRepository) {
        this.imageService = imageService;
        this.postService = postService;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    // 이미지 업로드, S3 URL 반환
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImages(@RequestParam("image") MultipartFile multipartFile, HttpServletRequest request) throws IOException {
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
            String uploadedUrl = imageService.uploadFileToS3(multipartFile, "image");

            return ResponseEntity.ok(uploadedUrl);
        }
    }

    // 이미지 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteImage(@RequestParam("postId") Long postId, @RequestParam("image") String imageUrl, HttpServletRequest request){
        postService.deleteImageFromPost(postId, imageUrl, request);

        return ResponseEntity.noContent().build();
    }
}
