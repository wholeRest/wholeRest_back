package org.example.rest_back.mypage.repository;

import org.example.rest_back.mypage.entity.Caution;
import org.example.rest_back.mypage.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CautionRepository extends JpaRepository<Caution, Integer> {
    List<Caution> findByEvent(Event event);
}
