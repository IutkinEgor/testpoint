package com.example.testpoint;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/user")
public class UserController {
    @GetMapping("1")
    public ResponseEntity<String> getValue1(){
        return new ResponseEntity<>("Test message 1", HttpStatus.OK);
    }

    @GetMapping("2")
    public ResponseEntity<String> getValue2(){
        return new ResponseEntity<>("Test message 2", HttpStatus.OK);
    }
}
