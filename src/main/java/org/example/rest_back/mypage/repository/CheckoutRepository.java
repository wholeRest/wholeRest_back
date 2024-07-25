package org.example.rest_back.mypage.repository;

import org.example.rest_back.mypage.entity.Checkup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckoutRepository extends JpaRepository<Checkup, Integer> {
}
