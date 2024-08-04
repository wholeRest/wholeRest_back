package org.example.rest_back.mypage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class EventImageDto {
    private MultipartFile morning_image_file;
    private MultipartFile lunch_image_file;
    private MultipartFile dinner_image_file;
}
