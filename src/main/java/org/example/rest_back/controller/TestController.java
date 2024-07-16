package org.example.rest_back.controller;

import org.example.rest_back.domain.Domain;
import org.example.rest_back.repository.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    private final Repository repository;
    public TestController(Repository repository) {
        this.repository = repository;
    }

    @GetMapping(path = "/test")
    public String test(){
        return "deploy test!";
    }



    @PostMapping
    public void create(@RequestBody Domain domain){
        this.repository.save(domain);
    }

    @GetMapping
    public List<Domain> getAllDomain(){
        return this.repository.findAll();
    }
}
