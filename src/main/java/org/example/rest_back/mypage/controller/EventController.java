package org.example.rest_back.mypage.controller;

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

    //get all events
    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAllEvents(){
        List<EventResponseDto> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    //get event by memberId
    @GetMapping("/{memberId}")
    public ResponseEntity<List<EventResponseDto>> getEventByMemberId(@PathVariable String memberId){
        List<EventResponseDto> events = eventService.getEventsByMemberId(memberId);
        return ResponseEntity.ok(events);
    }

    //get by memberId and Date
    @GetMapping("/date")
    public ResponseEntity<List<EventResponseDto>> getEventByMemberIdAndDate(@RequestParam String memberId, @RequestParam LocalDate date){
        List<EventResponseDto> events = eventService.getEventsByMemberIdAndDate(memberId, date);
        return ResponseEntity.ok(events);
    }

    //create event
    @PostMapping
    public ResponseEntity<Void> createEvent(@ModelAttribute EventDto eventDto) throws IOException {
        eventService.createEvent(eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //patch event
    @PatchMapping("/{eventId}")
    public ResponseEntity<Void> updateEvent(@PathVariable int eventId, @ModelAttribute EventDto eventDto) throws IOException {
        eventService.updateEvent(eventId, eventDto);
        return ResponseEntity.ok().build();
    }

    //delete event
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable int eventId){
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok().build();
    }
}
