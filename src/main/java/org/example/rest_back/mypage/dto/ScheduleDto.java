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
//    private String member_id;
    private LocalDate start_date;
    private LocalDate end_date;
    private String schedule_color;

    public static ScheduleDto from(Schedule schedule){
        ScheduleDto dto = new ScheduleDto();
        dto.setSchedule_id(schedule.getSchedule_id());
//        dto.setMember_id(schedule.getMember().getMember_id());
        dto.setStart_date(schedule.getStart_date());
        dto.setEnd_date(schedule.getEnd_date());
        dto.setSchedule_color(schedule.getSchedule_color());
        return dto;
    }
}

