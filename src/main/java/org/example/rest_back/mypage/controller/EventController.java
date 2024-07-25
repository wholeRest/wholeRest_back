package org.example.rest_back.mypage.controller;

import lombok.AllArgsConstructor;
import org.example.rest_back.mypage.dto.EventDto;
import org.example.rest_back.mypage.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/event")
public class EventController {
    private final EventService eventService;

    //get all events
    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents(){
        List<EventDto> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    //get event by calendarId
    @GetMapping("/{calendarId}")
    public ResponseEntity<List<EventDto>> getEventByCalendarId(@PathVariable int calendarId){
        List<EventDto> events = eventService.getEventsByCalendarId(calendarId);
        return ResponseEntity.ok(events);
    }

    //get by calendarId and Date
    @GetMapping("/date")
    public ResponseEntity<List<EventDto>> getEventByCalendarIdAndDate(@RequestParam int calendarId, @RequestParam int year, @RequestParam int month){
        List<EventDto> events = eventService.getEventsByCalendarIdAndDate(calendarId, year, month);
        return ResponseEntity.ok(events);
    }

    //create event
    @PostMapping
    public ResponseEntity<Void> createEvent(@RequestBody EventDto eventDto){
        eventService.createEvent(eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //patch event
    @PatchMapping("/{eventId}")
    public ResponseEntity<Void> updateEvent(@PathVariable int eventId, @RequestBody EventDto eventDto){
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
