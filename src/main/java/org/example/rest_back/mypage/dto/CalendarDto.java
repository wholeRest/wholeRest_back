package org.example.rest_back.mypage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.rest_back.mypage.entity.Calendar;

@Getter
@Setter
public class CalendarDto {
    private int calendar_id;
    private String member_id;

    public static CalendarDto from(Calendar calendar){
        CalendarDto dto = new CalendarDto();
        dto.setCalendar_id(calendar.getCalendar_id());
        dto.setMember_id(calendar.getMember().getMember_id());
        return dto;
    }
}
