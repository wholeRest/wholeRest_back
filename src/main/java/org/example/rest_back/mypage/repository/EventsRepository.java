package org.example.rest_back.mypage.repository;

import org.example.rest_back.mypage.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventsRepository extends JpaRepository<Events, Integer> {
}
