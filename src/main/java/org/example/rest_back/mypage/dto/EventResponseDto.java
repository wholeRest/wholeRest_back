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
        return responseDto;
    }
}
