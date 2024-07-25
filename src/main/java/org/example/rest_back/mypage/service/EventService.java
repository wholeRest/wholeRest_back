package org.example.rest_back.mypage.service;

import lombok.AllArgsConstructor;
import org.example.rest_back.mypage.dto.EventDto;
import org.example.rest_back.mypage.entity.Calendar;
import org.example.rest_back.mypage.entity.Event;
import org.example.rest_back.mypage.repository.CalendarRepository;
import org.example.rest_back.mypage.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final CalendarRepository calendarRepository;

    //get All
    public List<EventDto> getAllEvents(){
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(EventDto::from)
                .collect(Collectors.toList());
    }

    //get event by calendarId
    public List<EventDto> getEventsByCalendarId(int calendar_id){
        Calendar calendar = calendarRepository.findById(calendar_id).orElse(null);
        List<Event> events = eventRepository.findByCalendar(calendar);
        return events.stream()
                .map(EventDto::from)
                .collect(Collectors.toList());
    }

    //get by calendarId and Date
    public List<EventDto> getEventsByCalendarIdAndDate(int calendar_id, int year, int month){
        List<Event> events = eventRepository.findAllByCalendarIdAndYearAndMonth(calendar_id, year, month);
        return events.stream()
                .map(EventDto::from)
                .collect(Collectors.toList());
    }

    //post
    public void createEvent(EventDto eventDto){
        Calendar calendar = calendarRepository.findById(eventDto.getCalendar_id()).orElse(null);
        Event event = new Event();
        event.setCalendar(calendar);
        event.setDate(eventDto.getDate());
        event.setEmoji(eventDto.getEmoji());
        event.setToday_feel(eventDto.getToday_feel());
        event.setToday_condition(eventDto.getToday_condition());
        event.setToday_routine(eventDto.getToday_routine());
        event.setToday_appreciation(eventDto.getToday_appreciation());
        event.setMorning_image_url(eventDto.getMorning_image_url());
        event.setLunch_image_url(eventDto.getLunch_image_url());
        event.setDinner_image_url(eventDto.getDinner_image_url());
        event.setToday_memo(eventDto.getToday_memo());
        eventRepository.save(event);
    }

    //patch
    public void updateEvent(int event_id, EventDto eventDto){
        Event event = eventRepository.findById(event_id).orElse(null);
        if(event != null){
            if(eventDto.getEmoji() != null)
                event.setEmoji(eventDto.getEmoji());
            if(eventDto.getToday_feel() != null)
                event.setToday_feel(eventDto.getToday_feel());
            if(eventDto.getToday_condition() != null)
                event.setToday_condition(eventDto.getToday_condition());
            if(eventDto.getToday_routine() != null)
                event.setToday_routine(eventDto.getToday_routine());
            if(eventDto.getToday_appreciation() != null)
                event.setToday_appreciation(eventDto.getToday_appreciation());
            if(eventDto.getMorning_image_url() != null)
                event.setMorning_image_url(eventDto.getMorning_image_url());
            if(eventDto.getLunch_image_url() != null)
                event.setLunch_image_url(eventDto.getLunch_image_url());
            if(eventDto.getDinner_image_url() != null)
                event.setDinner_image_url(eventDto.getDinner_image_url());
            if(eventDto.getToday_memo() != null)
                event.setToday_memo(eventDto.getToday_memo());
            eventRepository.save(event);
        }
    }

    //delete
    public void deleteEvent(int event_id){
        eventRepository.deleteById(event_id);
    }

}
