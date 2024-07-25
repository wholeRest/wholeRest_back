package org.example.rest_back.mypage.controller;

import lombok.AllArgsConstructor;
import org.example.rest_back.mypage.dto.CalendarDto;
import org.example.rest_back.mypage.service.CalendarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(path="/api/calendar")
public class CalendarController {
    private final CalendarService calendarService;

    //Get
    @GetMapping("/{memberId}")
    public ResponseEntity<CalendarDto> getCalendarByMemberId(@PathVariable String memberId){
        CalendarDto calendarDto = calendarService.getCalendarByMemberId(memberId);
        return ResponseEntity.ok(calendarDto);
    }

    //Post
    @PostMapping
    public ResponseEntity<Void> createCalendar(@RequestBody CalendarDto calendarDto){
        calendarService.createCalendar(calendarDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
