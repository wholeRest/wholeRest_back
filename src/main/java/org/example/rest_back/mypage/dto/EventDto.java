
package org.example.rest_back.mypage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.rest_back.mypage.entity.Event;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class EventDto {
    private int event_id;
    private int calendar_id;
    private LocalDate date;
    private Integer emoji;
    private String today_feel;
    private String today_condition;
    private String today_routine;
    private String today_appreciation;
    private String morning_image_url;
    private String lunch_image_url;
    private String dinner_image_url;
    private String today_memo;

    public static EventDto from(Event event){
        EventDto dto = new EventDto();
        dto.setEvent_id(event.getEvent_id());
        dto.setCalendar_id(event.getCalendar().getCalendar_id());
        dto.setDate(event.getDate());
        dto.setEmoji(event.getEmoji());
        dto.setToday_feel(event.getToday_feel());
        dto.setToday_condition(event.getToday_condition());
        dto.setToday_routine(event.getToday_routine());
        dto.setToday_appreciation(event.getToday_appreciation());
        dto.setMorning_image_url(event.getMorning_image_url());
        dto.setLunch_image_url(event.getLunch_image_url());
        dto.setDinner_image_url(event.getDinner_image_url());
        dto.setToday_memo(event.getToday_memo());
        return dto;
    }
}
