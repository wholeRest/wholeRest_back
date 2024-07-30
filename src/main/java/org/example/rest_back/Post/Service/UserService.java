package org.example.rest_back.Post.Service;

import org.example.rest_back.Post.Domain.Users;
import org.example.rest_back.Post.Repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Optional<Users> getUserById(Long id) {
        return usersRepository.findById(id);
    }
}
