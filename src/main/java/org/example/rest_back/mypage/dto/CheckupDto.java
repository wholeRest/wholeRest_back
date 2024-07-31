
package org.example.rest_back.mypage.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.rest_back.mypage.entity.Checkup;

import java.time.LocalDate;

@Getter
@Setter
public class CheckupDto {
    private int checkup_id;
    private String content;
    private Boolean completed;
    private LocalDate create_time;

    public CheckupDto(){
        this.completed = false;
    }

    public static CheckupDto from(Checkup checkup){
        CheckupDto dto = new CheckupDto();
        dto.setCheckup_id(checkup.getCheckup_id());
        dto.setContent(checkup.getContent());
        dto.setCompleted(checkup.getCompleted());
        dto.setCreate_time(checkup.getCreate_time());
        return dto;
    }
}
