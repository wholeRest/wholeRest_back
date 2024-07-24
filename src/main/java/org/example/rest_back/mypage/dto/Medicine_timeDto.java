
package org.example.rest_back.mypage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.rest_back.mypage.entity.Medicine_time;
import java.time.LocalDate;

@Getter
@Setter
public class Medicine_timeDto {
    private int medicine_time_id;
    private int event_id;
    private String content;
    private Boolean completed;
    private LocalDate create_time;

    public Medicine_timeDto(){
        this.completed = false;
    }

    public static Medicine_timeDto from(Medicine_time medicine_time){
        Medicine_timeDto dto = new Medicine_timeDto();
        dto.setMedicine_time_id(medicine_time.getMedicine_time_id());
        dto.setEvent_id(medicine_time.getEvents().getEvent_id());
        dto.setContent(medicine_time.getContent());
        dto.setCompleted(medicine_time.getCompleted());
        dto.setCreate_time(medicine_time.getCreate_time());
        return dto;
    }
}
