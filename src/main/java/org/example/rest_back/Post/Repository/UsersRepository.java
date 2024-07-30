package org.example.rest_back.Post.Repository;

import org.example.rest_back.Post.Domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
