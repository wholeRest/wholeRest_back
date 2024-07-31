
package org.example.rest_back.mypage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.rest_back.mypage.entity.Event;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class EventDto {
    private int event_id;
    private LocalDate date;
    private Integer emoji = 0;
    private String today_feel;
    private String today_condition;
    private String today_routine;
    private String today_appreciation;
    private MultipartFile morning_image_file;
    private MultipartFile lunch_image_file;
    private MultipartFile dinner_image_file;
    private String today_memo;
}
