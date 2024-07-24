
package org.example.rest_back.mypage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.rest_back.mypage.entity.Events;
import org.example.rest_back.mypage.entity.Schedule;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class EventsDto {
    private int event_id;
    private int calendar_id;
    private LocalDate date;
    private int emoji;
    private int today_feel;
    private int today_condition;
    private String today_routine;
    private String today_appreciation;
    private String morning_image_url;
    private String lunch_image_url;
    private String dinner_image_url;
    private String today_memo;

    public static EventsDto from(Events events){
        EventsDto dto = new EventsDto();
        dto.setEvent_id(events.getEvent_id());
        dto.setCalendar_id(events.getCalendar().getCalendar_id());
        dto.setDate(events.getDate());
        dto.setEmoji(events.getEmoji());
        dto.setToday_feel(events.getToday_feel());
        dto.setToday_condition(events.getToday_condition());
        dto.setToday_routine(events.getToday_routine());
        dto.setToday_appreciation(events.getToday_appreciation());
        dto.setMorning_image_url(events.getMorning_image_url());
        dto.setLunch_image_url(events.getLunch_image_url());
        dto.setDinner_image_url(events.getDinner_image_url());
        dto.setToday_memo(events.getToday_memo());
        return dto;
    }
}
