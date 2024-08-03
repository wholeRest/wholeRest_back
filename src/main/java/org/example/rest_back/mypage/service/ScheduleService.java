package org.example.rest_back.mypage.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.example.rest_back.config.jwt.JwtUtils;
import org.example.rest_back.exception.NotFoundException;
import org.example.rest_back.exception.UnauthorizedException;
import org.example.rest_back.exception.UserNotFoundException;
import org.example.rest_back.mypage.dto.ScheduleDto;
import org.example.rest_back.mypage.entity.Schedule;
import org.example.rest_back.mypage.repository.ScheduleRepository;
import org.example.rest_back.user.domain.User;
import org.example.rest_back.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    //get by memberId
    public List<ScheduleDto> getSchedulesByMemberId(HttpServletRequest request){
        //token값을 통해 memberId 가져오기
        String token = jwtUtils.getJwtFromHeader(request);
        Claims claims = jwtUtils.getUserInfoFromToken(token);
        String memberId = claims.getSubject();

        //memberId로 user정보 가져오기
        User user = userRepository.findByMemberId(memberId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }

        List<Schedule> schedules = scheduleRepository.findByUser(user);
        return schedules.stream()
                .map(ScheduleDto::from)
                .collect(Collectors.toList());
    }

    //get by memberId and Date
    public List<ScheduleDto> getSchedulesByMemberIdAndDate(int year, int month, HttpServletRequest request){
        //token값을 통해 memberId 가져오기
        String token = jwtUtils.getJwtFromHeader(request);
        Claims claims = jwtUtils.getUserInfoFromToken(token);
        String memberId = claims.getSubject();

        //memberId로 user정보 가져오기
        User user = userRepository.findByMemberId(memberId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }

        //시작 날짜
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        //마지막 날짜
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        //유저 정보와 날짜를 통해 schedule정보 가져오기
        List<Schedule> schedules = scheduleRepository.findAllByUserAndYearAndMonth(user, year, month, startOfMonth, endOfMonth);
        return schedules.stream()
                .map(ScheduleDto::from)
                .collect(Collectors.toList());
    }

    //create
    public void createSchedule(ScheduleDto scheduleDto, HttpServletRequest request){
        //token값을 통해 memberId 가져오기
        String token = jwtUtils.getJwtFromHeader(request);
        Claims claims = jwtUtils.getUserInfoFromToken(token);
        String memberId = claims.getSubject();

        //memberId로 user정보 가져오기
        User user = userRepository.findByMemberId(memberId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }

        Schedule schedule = new Schedule();
        schedule.setUser(user);
        schedule.setStart_date(scheduleDto.getStart_date());
        schedule.setEnd_date(scheduleDto.getEnd_date());
        schedule.setSchedule_color(scheduleDto.getSchedule_color());
        schedule.setContent(scheduleDto.getContent());
        scheduleRepository.save(schedule);
    }

    //update by scheduleId
    public void updateSchedule(int schedule_id, ScheduleDto scheduleDto, HttpServletRequest request){
        //token값을 통해 memberId 가져오기
        String token = jwtUtils.getJwtFromHeader(request);
        Claims claims = jwtUtils.getUserInfoFromToken(token);
        String memberId = claims.getSubject();

        //memberId로 user정보 가져오기
        User user = userRepository.findByMemberId(memberId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }

        //schedule id값으로 schedule정보 가져오기
        Schedule schedule = scheduleRepository.findById(schedule_id).orElse(null);
        if(schedule == null){
            throw new NotFoundException("해당하는 schedule정보가 존재하지 않습니다.");
        }

        //토큰인증된 유저와 schedule작성자가 일치하는지 판별
        if(schedule.getUser() != user){
            throw new UnauthorizedException("사용자가 일치하지 않습니다.");
        }

        //scheduleDto에 정보가 있을 경우 수정
        if (scheduleDto.getStart_date() != null)
            schedule.setStart_date(scheduleDto.getStart_date());
        if(scheduleDto.getEnd_date() != null)
            schedule.setEnd_date(scheduleDto.getEnd_date());
        if(scheduleDto.getSchedule_color() != null)
            schedule.setSchedule_color(scheduleDto.getSchedule_color());
        if(scheduleDto.getContent() != null)
            schedule.setContent(scheduleDto.getContent());

        scheduleRepository.save(schedule);
    }

    //delete schedule
    public void deleteSchedule(int schedule_id, HttpServletRequest request) {
        //token값을 통해 memberId 가져오기
        String token = jwtUtils.getJwtFromHeader(request);
        Claims claims = jwtUtils.getUserInfoFromToken(token);
        String memberId = claims.getSubject();

        //memberId로 user정보 가져오기
        User user = userRepository.findByMemberId(memberId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }

        //schedule id값으로 schedule정보 가져오기
        Schedule schedule = scheduleRepository.findById(schedule_id).orElse(null);
        if(schedule == null){
            throw new NotFoundException("해당하는 schedule정보가 존재하지 않습니다.");
        }

        //토큰인증된 유저와 schedule작성자가 일치하는지 판별
        if(schedule.getUser() != user){
            throw new UnauthorizedException("사용자가 일치하지 않습니다.");
        }

        scheduleRepository.deleteById(schedule_id);
    }
}
