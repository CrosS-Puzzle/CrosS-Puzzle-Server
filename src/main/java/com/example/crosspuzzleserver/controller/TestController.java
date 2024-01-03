package com.example.crosspuzzleserver.controller;


import com.example.crosspuzzleserver.repository.CrossPuzzle;
import com.example.crosspuzzleserver.repository.TestRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestRepo testRepo;

    @GetMapping("/health")
    public String checkHealth() {
        return "OK";
    }

    @GetMapping("/testdb")
    public String testdb() {
        CrossPuzzle crossPuzzle = testRepo.findCrossPuzzleById("65956246b8817d0461363432");
        return crossPuzzle.getName();
    }

}
