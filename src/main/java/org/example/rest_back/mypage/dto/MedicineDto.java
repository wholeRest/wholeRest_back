
package org.example.rest_back.mypage.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.rest_back.mypage.entity.Medicine;
import java.time.LocalDate;

@Getter
@Setter
public class MedicineDto {
    private int medicine_id;
    private int event_id;
    private String content;
    private Boolean completed;
    private LocalDate create_time;

    public MedicineDto(){
        this.completed = false;
    }

    public static MedicineDto from(Medicine medicine){
        MedicineDto dto = new MedicineDto();
        dto.setMedicine_id(medicine.getMedicine_id());
        dto.setEvent_id(medicine.getEvent().getEvent_id());
        dto.setContent(medicine.getContent());
        dto.setCompleted(medicine.getCompleted());
        dto.setCreate_time(medicine.getCreate_time());
        return dto;
    }
}
