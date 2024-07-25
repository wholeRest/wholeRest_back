package org.example.rest_back.mypage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.rest_back.mypage.entity.Schedule;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleDto {
    private int schedule_id;
    private int calendar_id;
    private LocalDate start_id;
    private LocalDate end_id;
    private String schedule_color;

    public static ScheduleDto from(Schedule schedule){
        ScheduleDto dto = new ScheduleDto();
        dto.setSchedule_id(schedule.getSchedule_id());
        dto.setCalendar_id(schedule.getCalendar().getCalendar_id());
        dto.setStart_id(schedule.getStart_date());
        dto.setEnd_id(schedule.getEnd_date());
        dto.setSchedule_color(schedule.getSchedule_color());
        return dto;
    }
}

