package org.example.rest_back.mypage.service;

import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import org.example.rest_back.mypage.dto.EventDto;
import org.example.rest_back.mypage.dto.EventResponseDto;
import org.example.rest_back.mypage.entity.Event;
import org.example.rest_back.mypage.entity.Member;
import org.example.rest_back.mypage.repository.EventRepository;
import org.example.rest_back.mypage.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final FileUploadService fileUploadService;
    private final MemberRepository memberRepository;

    //get All
    public List<EventResponseDto> getAllEvents(){
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(EventResponseDto::from)
                .collect(Collectors.toList());
    }

    //get event by memberId
    public List<EventResponseDto> getEventsByMemberId(String member_id){
        Member member = memberRepository.findById(member_id).orElse(null);
        List<Event> events = eventRepository.findByMember(member);
        return events.stream()
                .map(EventResponseDto::from)
                .collect(Collectors.toList());
    }

    //get by memberId and Date
    public List<EventResponseDto> getEventsByMemberIdAndDate(String member_id, LocalDate date){
        List<Event> events = eventRepository.findAllByMemberIdAndDate(member_id, date);
        return events.stream()
                .map(EventResponseDto::from)
                .collect(Collectors.toList());
    }

    //post
    public void createEvent(EventDto eventDto) throws IOException {
        String morning_image_url = null;
        String lunch_image_url = null;
        String dinner_image_url = null;
        String morning_image_filename = null;
        String lunch_image_filename = null;
        String dinner_image_filename = null;

        //eventDto에 이미지 파일이 있는지 확인
        if(eventDto.getMorning_image_file() != null){
            //버킷에 파일 저장 후 url저장
            morning_image_url = fileUploadService.uploadFile(eventDto.getMorning_image_file());
            //원본 파일 이름 저장
            morning_image_filename = eventDto.getMorning_image_file().getOriginalFilename();
        }
        if(eventDto.getLunch_image_file() != null){
            lunch_image_url = fileUploadService.uploadFile(eventDto.getLunch_image_file());
            lunch_image_filename = eventDto.getLunch_image_file().getOriginalFilename();
        }
        if(eventDto.getDinner_image_file() != null){
            dinner_image_url = fileUploadService.uploadFile(eventDto.getDinner_image_file());
            dinner_image_filename = eventDto.getDinner_image_file().getOriginalFilename();
        }

        // member_id가 null인지 확인
        if (eventDto.getMember_id() == null) {
            throw new IllegalArgumentException("Member ID must not be null");
        }

        Member member = memberRepository.findById(eventDto.getMember_id()).orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Event event = new Event();
        event.setMember(member);
        event.setDate(eventDto.getDate());
        event.setEmoji(eventDto.getEmoji());
        event.setToday_feel(eventDto.getToday_feel());
        event.setToday_condition(eventDto.getToday_condition());
        event.setToday_routine(eventDto.getToday_routine());
        event.setToday_appreciation(eventDto.getToday_appreciation());
        event.setMorning_image_url(morning_image_url);
        event.setLunch_image_url(lunch_image_url);
        event.setDinner_image_url(dinner_image_url);
        event.setMorning_image_filename(morning_image_filename);
        event.setLunch_image_filename(lunch_image_filename);
        event.setDinner_image_filename(dinner_image_filename);
        event.setToday_memo(eventDto.getToday_memo());
        eventRepository.save(event);
    }

    //patch
    public void updateEvent(int event_id, EventDto eventDto) throws IOException {
        Event event = eventRepository.findById(event_id).orElse(null);

        String morning_image_url = event.getMorning_image_url();
        String lunch_image_url = event.getLunch_image_url();
        String dinner_image_url = event.getDinner_image_url();
        String morning_image_filename = event.getMorning_image_filename();
        String lunch_image_filename = event.getLunch_image_filename();
        String dinner_image_filename = event.getDinner_image_filename();

        //eventDto에 이미지 파일이 있는지 확인
        if(eventDto.getMorning_image_file() == null){
            if(morning_image_filename != null) {
                //dto에 이미지가 없고 기존 DB에 이미지가 없다면 버킷에서 이미지 파일 제거
                fileUploadService.deleteFile(morning_image_filename);
                morning_image_url = null;
                morning_image_filename = null;
            }
        } else{
            //버킷에 파일 저장 후 url저장
            morning_image_url = fileUploadService.uploadFile(eventDto.getMorning_image_file());
            //원본 파일 이름 저장
            morning_image_filename = eventDto.getMorning_image_file().getOriginalFilename();
        }

        if(eventDto.getLunch_image_file() == null){
            if(lunch_image_filename != null) {
                fileUploadService.deleteFile(lunch_image_filename);
                lunch_image_url = null;
                lunch_image_filename = null;
            }
        } else{
            lunch_image_url = fileUploadService.uploadFile(eventDto.getLunch_image_file());
            lunch_image_filename = eventDto.getLunch_image_file().getOriginalFilename();
        }

        if(eventDto.getDinner_image_file() == null){
            if(dinner_image_filename != null) {
                fileUploadService.deleteFile(dinner_image_filename);
                dinner_image_url = null;
                dinner_image_filename = null;
            }
        } else{
            dinner_image_url = fileUploadService.uploadFile(eventDto.getDinner_image_file());
            dinner_image_filename = eventDto.getDinner_image_file().getOriginalFilename();

        }

        if(eventDto != null){
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

            //아침 사진
            event.setMorning_image_url(morning_image_url);
            event.setMorning_image_filename(morning_image_filename);
            //점심 사진
            event.setLunch_image_url(lunch_image_url);
            event.setLunch_image_filename(lunch_image_filename);
            //저녁 사진
            event.setDinner_image_url(dinner_image_url);
            event.setDinner_image_filename(dinner_image_filename);

            if(eventDto.getToday_memo() != null)
                event.setToday_memo(eventDto.getToday_memo());
            eventRepository.save(event);
        }
    }

    //delete
    public void deleteEvent(int event_id){
        Event event = eventRepository.findById(event_id).orElse(null);

        String morning_image_filename = event.getMorning_image_filename();
        String lunch_image_filename = event.getLunch_image_filename();
        String dinner_image_filename = event.getDinner_image_filename();

        //DB에 사진이 저장되어 있을 경우 버킷에서 삭제
        if(morning_image_filename != null)
            fileUploadService.deleteFile(morning_image_filename);
        if(lunch_image_filename != null)
            fileUploadService.deleteFile(lunch_image_filename);
        if(dinner_image_filename != null)
            fileUploadService.deleteFile(dinner_image_filename);

        eventRepository.deleteById(event_id);
    }

}
