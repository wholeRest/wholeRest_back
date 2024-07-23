package org.example.rest_back.Post.Repository;

import org.example.rest_back.Post.Domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByCategory(String category);

}
