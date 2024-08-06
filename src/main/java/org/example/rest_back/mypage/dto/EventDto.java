
package org.example.rest_back.mypage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class EventDto {
    private int event_id;
    @NotBlank(message = "date필드에 값이 존재하지 않거나 공백이 포함되어 있습니다.")
    private LocalDate date;
    private String today_feel;
    private String today_condition;
    private String today_routine;
    private String today_appreciation;
    private String today_memo;

    //투두
    private String todoContent1;
    private String todoContent2;

    //약 복용 시간
    private String medicineContent1;
    private String medicineContent2;
    private String medicineContent3;
    private String medicineContent4;

    //검진 결과
    private String checkupContent1;
    private String checkupContent2;
    private String checkupContent3;
    private String checkupContent4;

    //주의 사항
    private String cautionContent1;
    private String cautionContent2;
    private String cautionContent3;
    private String cautionContent4;
}
