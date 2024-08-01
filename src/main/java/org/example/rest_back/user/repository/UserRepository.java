package org.example.rest_back.user.repository;

import org.example.rest_back.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByMemberId(String memberId);
    Optional<User> findByNameAndDateOfBirth(String name, String dateOfBirth);
    Optional<User> findByMemberIdAndNameAndDateOfBirth(String memberId, String name, String dateofBirth);
}
