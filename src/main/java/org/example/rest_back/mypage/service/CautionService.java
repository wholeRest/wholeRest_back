package org.example.rest_back.mypage.service;

import lombok.AllArgsConstructor;
import org.example.rest_back.mypage.dto.CautionDto;
import org.example.rest_back.mypage.entity.Caution;
import org.example.rest_back.mypage.entity.Event;
import org.example.rest_back.mypage.repository.CautionRepository;
import org.example.rest_back.mypage.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CautionService {
    private final CautionRepository cautionRepository;
    private final EventRepository eventRepository;

    //get caution by eventId
    public List<CautionDto> getCautionByEventId(int event_id){
        Event event = eventRepository.findById(event_id).orElse(null);
        List<Caution> cautions = cautionRepository.findByEvent(event);
        return cautions.stream()
                .map(CautionDto::from)
                .collect(Collectors.toList());
    }

    //create caution
    public void createCaution(int eventId, CautionDto cautionDto){
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null)
            return;

        Caution caution = new Caution();
        caution.setEvent(event);
        caution.setContent(cautionDto.getContent());
        caution.setCompleted(cautionDto.getCompleted());
        cautionRepository.save(caution);
    }

    //update caution
    public void updateCaution(int caution_id, CautionDto cautionDto){
        Caution caution = cautionRepository.findById(caution_id).orElse(null);
        if (caution != null){
            if(cautionDto.getContent() != null)
                caution.setContent(cautionDto.getContent());
            if(cautionDto.getCompleted() != null)
                caution.setCompleted(cautionDto.getCompleted());
            cautionRepository.save(caution);
        }
    }

    //Delete caution
    public void deleteCaution(int caution_id){
        cautionRepository.deleteById(caution_id);
    }
}
