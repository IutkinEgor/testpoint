package com.example.testpoint;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/test")
public class TestController {

    @Value("${app.test}")
    private Boolean test;

    @GetMapping("1")
    public ResponseEntity<String> getValue1(){
        return new ResponseEntity<>("Test message 1: " + test, HttpStatus.OK);
    }

    @GetMapping("2")
    public ResponseEntity<String> getValue2(){
        return new ResponseEntity<>("Test message 2", HttpStatus.OK);
    }

    @GetMapping("3")
    public ResponseEntity<String> getValue3(){
        return new ResponseEntity<>("Test message 3", HttpStatus.OK);
    }
    @GetMapping("4")
    public ResponseEntity<String> getValue5(){
        return new ResponseEntity<>("Test message 6", HttpStatus.OK);
    }

}
