package org.example.rest_back.mypage.service;

import lombok.AllArgsConstructor;
import org.example.rest_back.mypage.dto.ScheduleDto;
import org.example.rest_back.mypage.entity.Member;
import org.example.rest_back.mypage.entity.Schedule;
import org.example.rest_back.mypage.repository.MemberRepository;
import org.example.rest_back.mypage.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;

    //get All
    public List<ScheduleDto> getAllSchedules(){
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream()
                .map(ScheduleDto::from)
                .collect(Collectors.toList());
    }

    //get by memberId
    public List<ScheduleDto> getSchedulesByMemberId(String member_id){
        Member member = memberRepository.findById(member_id).orElse(null);
        List<Schedule> schedules = scheduleRepository.findByMember(member);
        return schedules.stream()
                .map(ScheduleDto::from)
                .collect(Collectors.toList());
    }

    //get by memberId and Date
    public List<ScheduleDto> getSchedulesByMemberIdAndDate(String member_id, int year, int month){
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
        List<Schedule> schedules = scheduleRepository.findAllByMemberIdAndYearAndMonth(member_id, year, month, startOfMonth, endOfMonth);
        return schedules.stream()
                .map(ScheduleDto::from)
                .collect(Collectors.toList());
    }

    //create
    public void createSchedule(ScheduleDto scheduleDto){
        Member member = memberRepository.findById(scheduleDto.getMember_id()).orElse(null);
        Schedule schedule = new Schedule();
        schedule.setMember(member);
        schedule.setStart_date(scheduleDto.getStart_date());
        schedule.setEnd_date(scheduleDto.getEnd_date());
        schedule.setSchedule_color(scheduleDto.getSchedule_color());
        scheduleRepository.save(schedule);
    }

    //update by scheduleId
    public void updateSchedule(int schedule_id, ScheduleDto scheduleDto){
        Schedule schedule = scheduleRepository.findById(schedule_id).orElse(null);
        if(schedule != null){
            if(scheduleDto.getStart_date() != null)
                schedule.setStart_date(scheduleDto.getStart_date());
            if(scheduleDto.getEnd_date() != null)
                schedule.setEnd_date(scheduleDto.getEnd_date());
            if(scheduleDto.getSchedule_color() != null)
                schedule.setSchedule_color(scheduleDto.getSchedule_color());
            scheduleRepository.save(schedule);
        }
    }

    //delete schedule
    public void deleteSchedule(int schedule_id) {
        scheduleRepository.deleteById(schedule_id);
    }
}
