package org.example.rest_back.mypage.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.example.rest_back.mypage.dto.EventDto;
import org.example.rest_back.mypage.dto.EventResponseDto;
import org.example.rest_back.mypage.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/event")
public class EventController {
    private final EventService eventService;

    //get event by memberId
    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getEventByMemberId(HttpServletRequest request){
        List<EventResponseDto> events = eventService.getEventsByMemberId(request);
        return ResponseEntity.ok(events);
    }

    //get by memberId and Date
    @GetMapping("/date")
    public ResponseEntity<List<EventResponseDto>> getEventByMemberIdAndDate(@RequestParam LocalDate date, HttpServletRequest request){
        List<EventResponseDto> events = eventService.getEventsByMemberIdAndDate(date, request);
        return ResponseEntity.ok(events);
    }

    //create event
    @PostMapping
    public ResponseEntity<Void> createEvent(@ModelAttribute EventDto eventDto, HttpServletRequest request) throws IOException {
        eventService.createEvent(eventDto, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //patch event
    @PatchMapping("/{eventId}")
    public ResponseEntity<Void> updateEvent(@PathVariable int eventId, @ModelAttribute EventDto eventDto, HttpServletRequest request) throws IOException {
        eventService.updateEvent(eventId, eventDto, request);
        return ResponseEntity.ok().build();
    }

    //delete event
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable int eventId, HttpServletRequest request){
        eventService.deleteEvent(eventId, request);
        return ResponseEntity.ok().build();
    }

    //edelte event image
    @DeleteMapping("/image/{eventId}")
    public ResponseEntity<Void> deleteEventImage(@PathVariable int eventId, @RequestParam String image, HttpServletRequest request){
        eventService.deleteEventImage(eventId, image, request);
        return ResponseEntity.ok().build();
    }
}
