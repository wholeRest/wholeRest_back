package org.example.rest_back.mypage.repository;

import org.example.rest_back.mypage.entity.Todo;
import org.example.rest_back.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> findByUser(User user);
}
