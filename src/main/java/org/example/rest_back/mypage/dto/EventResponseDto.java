package org.example.rest_back.mypage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.rest_back.mypage.entity.Event;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class EventResponseDto {
    private int event_id;
    private LocalDate date;
    private String today_feel;
    private String today_condition;
    private String today_routine;
    private String today_appreciation;
    private String morning_image_url;
    private String lunch_image_url;
    private String dinner_image_url;
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

    public static EventResponseDto from(Event event){
        EventResponseDto responseDto = new EventResponseDto();
        responseDto.setEvent_id(event.getEvent_id());
        responseDto.setDate(event.getDate());
        responseDto.setToday_feel(event.getToday_feel());
        responseDto.setToday_condition(event.getToday_condition());
        responseDto.setToday_routine(event.getToday_routine());
        responseDto.setToday_appreciation(event.getToday_appreciation());
        responseDto.setMorning_image_url(event.getMorning_image_url());
        responseDto.setLunch_image_url(event.getLunch_image_url());
        responseDto.setDinner_image_url(event.getDinner_image_url());
        responseDto.setToday_memo(event.getToday_memo());

        //투두
        responseDto.setTodoContent1(event.getTodoContent1());
        responseDto.setTodoContent2(event.getTodoContent2());

        //약 복용 시간
        responseDto.setMedicineContent1(event.getMedicineContent1());
        responseDto.setMedicineContent2(event.getMedicineContent2());
        responseDto.setMedicineContent3(event.getMedicineContent3());
        responseDto.setMedicineContent4(event.getMedicineContent4());

        //검진 결과
        responseDto.setCheckupContent1(event.getCheckupContent1());
        responseDto.setCheckupContent2(event.getCheckupContent2());
        responseDto.setCheckupContent3(event.getCheckupContent3());
        responseDto.setCheckupContent4(event.getCheckupContent4());

        //주의 사항
        responseDto.setCautionContent1(event.getCautionContent1());
        responseDto.setCautionContent2(event.getCautionContent2());
        responseDto.setCautionContent3(event.getCautionContent3());
        responseDto.setCautionContent4(event.getCautionContent4());

        return responseDto;
    }
}
