package org.example.rest_back.mypage.repository;

import org.example.rest_back.mypage.entity.Caution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CautionRepository extends JpaRepository<Caution, Integer> {
}
