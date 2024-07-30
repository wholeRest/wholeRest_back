package org.example.rest_back.Post.Controller;

import jakarta.validation.Valid;
import org.example.rest_back.Post.Domain.Likes;
import org.example.rest_back.Post.Dto.LikesDto;
import org.example.rest_back.Post.Service.LikesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikesController {
    private final LikesService likesService;

    public LikesController(LikesService likesService) {
        this.likesService = likesService;
    }

    // 좋아요 추가
    @PostMapping
    public ResponseEntity<LikesDto> addLikes(@ModelAttribute LikesDto likesDto) throws Exception {
        Likes likes = likesService.insertLike(likesDto);
        return ResponseEntity.ok(likesService.convertToDto(likes));
    }

    // 좋아요 취소
    @DeleteMapping
    public ResponseEntity<Void> deleteLikes(@ModelAttribute LikesDto likesDto) {
        likesService.deleteLike(likesDto);
        return ResponseEntity.noContent().build();
    }
}
