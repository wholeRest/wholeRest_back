package org.example.rest_back.mypage.service;

import lombok.AllArgsConstructor;
import org.example.rest_back.mypage.dto.CheckupDto;
import org.example.rest_back.mypage.entity.Checkup;
import org.example.rest_back.mypage.entity.Event;
import org.example.rest_back.mypage.repository.CheckupRepository;
import org.example.rest_back.mypage.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CheckupService {
    private final CheckupRepository checkupRepository;
    private final EventRepository eventRepository;

    //get checkup by eventId
    public List<CheckupDto> getCheckupByEventId(int event_id){
        Event event = eventRepository.findById(event_id).orElse(null);
        List<Checkup> checkups = checkupRepository.findByEvent(event);
        return checkups.stream()
                .map(CheckupDto::from)
                .collect(Collectors.toList());
    }

    //create checkup
    public void createCheckup(int eventId, CheckupDto checkupDto){
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null)
            return;

        Checkup checkup = new Checkup();
        checkup.setEvent(event);
        checkup.setContent(checkupDto.getContent());
        checkup.setCompleted(checkupDto.getCompleted());
        checkup.setCompleted(checkupDto.getCompleted());
        checkupRepository.save(checkup);
    }

    //update checkup
    public void updateCheckup(int checkup_id, CheckupDto checkupDto){
        Checkup checkup = checkupRepository.findById(checkup_id).orElse(null);
        if (checkup != null){
            if(checkupDto.getContent() != null)
                checkup.setContent(checkupDto.getContent());
            if(checkupDto.getCompleted() != null)
                checkup.setCompleted(checkupDto.getCompleted());
            checkupRepository.save(checkup);
        }
    }

    //Delete checkup
    public void deleteCheckup(int checkup_id){
        checkupRepository.deleteById(checkup_id);
    }
}
