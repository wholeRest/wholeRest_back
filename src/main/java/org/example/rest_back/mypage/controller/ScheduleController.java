package org.example.rest_back.mypage.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.example.rest_back.mypage.dto.ScheduleDto;
import org.example.rest_back.mypage.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    //get schedule by year and month
    @GetMapping
    public ResponseEntity<List<ScheduleDto>> getScheduleByMemberIdAndMonth(@RequestParam String yearMonth, HttpServletRequest request){
        List<ScheduleDto> schedules = scheduleService.getSchedulesByMemberIdAndMonth(yearMonth, request);
        return ResponseEntity.ok(schedules);
    }

    //get schedule by date
    @GetMapping("/date")
    public ResponseEntity<List<ScheduleDto>> getScheduleByMemberIdAndDate(@RequestParam LocalDate date, HttpServletRequest request){
        List<ScheduleDto> schedules = scheduleService.getSchedulesByMemberIdAndDate(date, request);
        return ResponseEntity.ok(schedules);
    }

    //create schedule
    @PostMapping
    public ResponseEntity<Void> createSchedule(@RequestBody ScheduleDto scheduleDto, HttpServletRequest request){
        scheduleService.createSchedule(scheduleDto, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //patch schedule
    @PatchMapping("/{scheduleId}")
    public ResponseEntity<Void> updateSchedule(@PathVariable int scheduleId, @RequestBody ScheduleDto scheduleDto, HttpServletRequest request){
        scheduleService.updateSchedule(scheduleId, scheduleDto, request);
        return ResponseEntity.ok().build();
    }

    //delete schedule
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable int scheduleId, HttpServletRequest request){
        scheduleService.deleteSchedule(scheduleId, request);
        return ResponseEntity.ok().build();
    }
}
