package org.example.rest_back.Post.Controller;

import org.example.rest_back.Post.Service.ImageService;
import org.example.rest_back.Post.Service.PostService;
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

    @Autowired
    public ImageController(ImageService imageService, PostService postService) {
        this.imageService = imageService;
        this.postService = postService;
    }

    // 이미지 업로드, S3 URL 반환
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImages(@RequestParam("image") MultipartFile multipartFile) throws IOException {
        String uploadedUrl = imageService.uploadFileToS3(multipartFile, "image");

        return ResponseEntity.ok(uploadedUrl);
    }

    // 이미지 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteImage(@RequestParam("postId") Long postId, @RequestParam("image") String imageUrl){
        postService.deleteImageFromPost(postId, imageUrl);

        return ResponseEntity.noContent().build();
    }
}
