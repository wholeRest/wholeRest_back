package org.example.rest_back.mypage.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.rest_back.mypage.entity.Caution;

import java.time.LocalDate;

@Getter
@Setter
public class CautionDto {
    private int caution_id;
    private String content;
    private Boolean completed;
    private LocalDate create_time;

    public CautionDto(){
        this.completed = false;
    }

    public static CautionDto from(Caution caution){
        CautionDto dto = new CautionDto();
        dto.setCaution_id(caution.getCaution_id());
        dto.setContent(caution.getContent());
        dto.setCompleted(caution.getCompleted());
        dto.setCreate_time(caution.getCreate_time());
        return dto;
    }
}
