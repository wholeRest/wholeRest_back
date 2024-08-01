package org.example.rest_back.mypage.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.example.rest_back.config.jwt.JwtUtils;
import org.example.rest_back.exception.NotFoundException;
import org.example.rest_back.exception.UnauthorizedException;
import org.example.rest_back.exception.UserNotFoundException;
import org.example.rest_back.mypage.dto.EventDto;
import org.example.rest_back.mypage.dto.EventResponseDto;
import org.example.rest_back.mypage.entity.Event;
import org.example.rest_back.mypage.repository.EventRepository;
import org.example.rest_back.user.domain.User;
import org.example.rest_back.user.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    //get event by memberId
    public List<EventResponseDto> getEventsByMemberId(HttpServletRequest request){
        //token값을 통해 memberId 가져오기
        String token = jwtUtils.getJwtFromHeader(request);
        Claims claims = jwtUtils.getUserInfoFromToken(token);
        String memberId = claims.getSubject();

        //memberId로 user정보 가져오기
        User user = userRepository.findByMemberId(memberId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }

        List<Event> events = eventRepository.findByUser(user);
        return events.stream()
                .map(EventResponseDto::from)
                .collect(Collectors.toList());
    }

    //get by memberId and Date
    public List<EventResponseDto> getEventsByMemberIdAndDate(LocalDate date, HttpServletRequest request){
        //token값을 통해 memberId 가져오기
        String token = jwtUtils.getJwtFromHeader(request);
        Claims claims = jwtUtils.getUserInfoFromToken(token);
        String memberId = claims.getSubject();

        //memberId로 user정보 가져오기
        User user = userRepository.findByMemberId(memberId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }

        //user정보로 해당하는 event리스트 가져오기
        List<Event> events = eventRepository.findAllByUserAndDate(user, date);
        return events.stream()
                .map(EventResponseDto::from)
                .collect(Collectors.toList());
    }

    //post
    public void createEvent(EventDto eventDto, HttpServletRequest request) throws IOException {
        //token값을 통해 memberId 가져오기
        String token = jwtUtils.getJwtFromHeader(request);
        Claims claims = jwtUtils.getUserInfoFromToken(token);
        String memberId = claims.getSubject();

        //memberId로 user정보 가져오기
        User user = userRepository.findByMemberId(memberId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }

        String morning_image_url = null;
        String lunch_image_url = null;
        String dinner_image_url = null;
        String morning_image_filename = null;
        String lunch_image_filename = null;
        String dinner_image_filename = null;

        //eventDto에 이미지 파일이 있는지 확인 후 존재한다면 버킷에 업로드
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

        Event event = new Event();
        event.setUser(user);
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
    public void updateEvent(int event_id, EventDto eventDto, HttpServletRequest request) throws IOException {
        //token값을 통해 memberId 가져오기
        String token = jwtUtils.getJwtFromHeader(request);
        Claims claims = jwtUtils.getUserInfoFromToken(token);
        String memberId = claims.getSubject();

        //memberId로 user정보 가져오기
        User user = userRepository.findByMemberId(memberId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }

        //event id값으로 event정보 가져오기
        Event event = eventRepository.findById(event_id).orElse(null);
        if(event == null){
            throw new NotFoundException("해당하는 event정보가 존재하지 않습니다.");
        }

        //토큰인증된 유저와 event작성자가 일치하는지 판별
        if(event.getUser() != user){
            throw new UnauthorizedException("사용자가 일치하지 않습니다.");
        }

        String morning_image_url = event.getMorning_image_url();
        String lunch_image_url = event.getLunch_image_url();
        String dinner_image_url = event.getDinner_image_url();
        String morning_image_filename = event.getMorning_image_filename();
        String lunch_image_filename = event.getLunch_image_filename();
        String dinner_image_filename = event.getDinner_image_filename();

        //eventDto에 이미지 파일이 있는지 확인
        if(eventDto.getMorning_image_file() != null){
            //기존 DB에 이미지가 있을 경우
            if(morning_image_filename != null){
                //기존 DB 이미지와 새로 받은 이미지가 다를 경우
                if(!morning_image_filename.equals(eventDto.getMorning_image_file().getOriginalFilename())){
                    //기존 이미지 제거
                    fileUploadService.deleteFile(morning_image_filename);
                    //버킷에 파일 저장 후 url저장
                    morning_image_url = fileUploadService.uploadFile(eventDto.getMorning_image_file());
                    //원본 파일 이름 저장
                    morning_image_filename = eventDto.getMorning_image_file().getOriginalFilename();
                }
            }
        }

        if(eventDto.getLunch_image_file() != null){
            if(lunch_image_filename != null){
                //기존 DB 이미지와 새로 받은 이미지가 다를 경우
                if(!lunch_image_filename.equals(eventDto.getLunch_image_file().getOriginalFilename())){
                    //기존 이미지 제거
                    fileUploadService.deleteFile(lunch_image_filename);
                    //버킷에 파일 저장 후 url저장
                    lunch_image_url = fileUploadService.uploadFile(eventDto.getLunch_image_file());
                    //원본 파일 이름 저장
                    lunch_image_filename = eventDto.getLunch_image_file().getOriginalFilename();
                }
            }
        }

        if(eventDto.getDinner_image_file() != null){
            if(dinner_image_filename != null){
                //기존 DB 이미지와 새로 받은 이미지가 다를 경우
                if(!dinner_image_filename.equals(eventDto.getDinner_image_file().getOriginalFilename())){
                    //기존 이미지 제거
                    fileUploadService.deleteFile(dinner_image_filename);
                    //버킷에 파일 저장 후 url저장
                    dinner_image_url = fileUploadService.uploadFile(eventDto.getDinner_image_file());
                    //원본 파일 이름 저장
                    dinner_image_filename = eventDto.getDinner_image_file().getOriginalFilename();
                }
            }
        }

        //event 수정
        if (eventDto.getEmoji() != null)
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

    //delete
    public void deleteEvent(int event_id, HttpServletRequest request){
        //token값을 통해 memberId 가져오기
        String token = jwtUtils.getJwtFromHeader(request);
        Claims claims = jwtUtils.getUserInfoFromToken(token);
        String memberId = claims.getSubject();

        //memberId로 user정보 가져오기
        User user = userRepository.findByMemberId(memberId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }

        //event id값으로 event정보 가져오기
        Event event = eventRepository.findById(event_id).orElse(null);
        if(event == null){
            throw new NotFoundException("해당하는 event정보가 존재하지 않습니다.");
        }

        //토큰인증된 유저와 event작성자가 일치하는지 판별
        if(event.getUser() != user){
            throw new UnauthorizedException("사용자가 일치하지 않습니다.");
        }

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

    //delete image
    public void deleteEventImage(int event_id, String image, HttpServletRequest request){
        //token값을 통해 memberId 가져오기
        String token = jwtUtils.getJwtFromHeader(request);
        Claims claims = jwtUtils.getUserInfoFromToken(token);
        String memberId = claims.getSubject();

        //memberId로 user정보 가져오기
        User user = userRepository.findByMemberId(memberId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }

        //event id값으로 event정보 가져오기
        Event event = eventRepository.findById(event_id).orElse(null);
        if(event == null){
            throw new NotFoundException("해당하는 event정보가 존재하지 않습니다.");
        }

        //토큰인증된 유저와 event작성자가 일치하는지 판별
        if(event.getUser() != user){
            throw new UnauthorizedException("사용자가 일치하지 않습니다.");
        }

        String morning_image_filename = event.getMorning_image_filename();
        String lunch_image_filename = event.getLunch_image_filename();
        String dinner_image_filename = event.getDinner_image_filename();

        switch (image) {
            case "morningImage" -> {
                //DB에 사진이 저장되어 있을 경우 버킷에서 삭제
                if (morning_image_filename != null) {
                    fileUploadService.deleteFile(morning_image_filename);
                    event.setMorning_image_filename(null);
                    event.setMorning_image_url(null);
                }
            }
            case "lunchImage" -> {
                if (lunch_image_filename != null) {
                    fileUploadService.deleteFile(lunch_image_filename);
                    event.setLunch_image_filename(null);
                    event.setLunch_image_url(null);
                }
            }
            case "dinnerImage" -> {
                if (dinner_image_filename != null) {
                    fileUploadService.deleteFile(dinner_image_filename);
                    event.setDinner_image_filename(null);
                    event.setDinner_image_url(null);
                }
            }
        }

        eventRepository.save(event);
    }
}
