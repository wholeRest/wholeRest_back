package org.example.rest_back.mypage.repository;

import org.example.rest_back.mypage.entity.Event;
import org.example.rest_back.mypage.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
    List<Medicine> findByEvent(Event event);
}
