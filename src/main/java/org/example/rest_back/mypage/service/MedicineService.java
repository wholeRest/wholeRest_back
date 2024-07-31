package org.example.rest_back.mypage.service;

import lombok.AllArgsConstructor;
import org.example.rest_back.mypage.dto.MedicineDto;
import org.example.rest_back.mypage.entity.Event;
import org.example.rest_back.mypage.entity.Medicine;
import org.example.rest_back.mypage.repository.EventRepository;
import org.example.rest_back.mypage.repository.MedicineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MedicineService {
    private final MedicineRepository medicineRepository;
    private final EventRepository eventRepository;

    //get medicine by eventId
    public List<MedicineDto> getMedicineByEventId(int event_id){
        Event event = eventRepository.findById(event_id).orElse(null);
        List<Medicine> medicines = medicineRepository.findByEvent(event);
        return medicines.stream()
                .map(MedicineDto::from)
                .collect(Collectors.toList());
    }

    //create medicine
    public void createMedicine(int eventId, MedicineDto medicineDto){
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null)
            return;

        Medicine medicine = new Medicine();
        medicine.setEvent(event);
        medicine.setContent(medicineDto.getContent());
        medicine.setCompleted(medicineDto.getCompleted());
        medicine.setCompleted(medicineDto.getCompleted());
        medicineRepository.save(medicine);
    }

    //update medicine
    public void updateMedicine(int medicine_id, MedicineDto medicineDto){
        Medicine medicine = medicineRepository.findById(medicine_id).orElse(null);
        if (medicine != null){
            if(medicineDto.getContent() != null)
                medicine.setContent(medicineDto.getContent());
            if(medicineDto.getCompleted() != null)
                medicine.setCompleted(medicineDto.getCompleted());
            medicineRepository.save(medicine);
        }
    }

    //Delete
    public void deleteMedicine(int medicine_id){
        medicineRepository.deleteById(medicine_id);
    }
}