package org.example.rest_back.mypage.service;

import lombok.AllArgsConstructor;
import org.example.rest_back.mypage.dto.ScheduleDto;
import org.example.rest_back.mypage.entity.Calendar;
import org.example.rest_back.mypage.entity.Schedule;
import org.example.rest_back.mypage.repository.CalendarRepository;
import org.example.rest_back.mypage.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final CalendarRepository calendarRepository;

    //get All
    public List<ScheduleDto> getAllSchedules(){
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream()
                .map(ScheduleDto::from)
                .collect(Collectors.toList());
    }

    //get by calendarId
    public List<ScheduleDto> getSchedulesByCalendarId(int calendar_id){
        Calendar calendar = calendarRepository.findById(calendar_id).orElse(null);
        List<Schedule> schedules = scheduleRepository.findByCalendar(calendar);
        return schedules.stream()
                .map(ScheduleDto::from)
                .collect(Collectors.toList());
    }

    //get by calendarId and Date
    public List<ScheduleDto> getSchedulesByCalendarIdAndDate(int calendar_id, int year, int month){
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
        List<Schedule> schedules = scheduleRepository.findAllByCalendarIdAndYearAndMonth(calendar_id, year, month, startOfMonth, endOfMonth);
        return schedules.stream()
                .map(ScheduleDto::from)
                .collect(Collectors.toList());
    }

    //update by scheduleId
    public void updateCalendar(int schedule_id, ScheduleDto scheduleDto){
        Schedule schedule = scheduleRepository.findById(schedule_id).orElse(null);
        if(schedule != null){
            if(scheduleDto.getStart_id() != null)
                schedule.setStart_date(scheduleDto.getStart_id());
            if(scheduleDto.getEnd_id() != null)
                schedule.setEnd_date(scheduleDto.getEnd_id());
            if(scheduleDto.getSchedule_color() != null)
                schedule.setSchedule_color(scheduleDto.getSchedule_color());
            scheduleRepository.save(schedule);
        }
    }

    //delete schedule
    public void deleteCalendar(int schedule_id) {
        scheduleRepository.deleteById(schedule_id);
    }
}
