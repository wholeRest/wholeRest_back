package org.example.rest_back.mypage.repository;

import org.example.rest_back.mypage.entity.Checkup;
import org.example.rest_back.mypage.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckupRepository extends JpaRepository<Checkup, Integer> {
    List<Checkup> findByEvent(Event event);
}
