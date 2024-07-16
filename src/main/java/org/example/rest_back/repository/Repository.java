package org.example.rest_back.repository;

import org.example.rest_back.domain.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repository extends JpaRepository<Domain, Integer> {
}
