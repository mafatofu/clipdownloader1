package com.example.clipdownloader1.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//aws 체크용 controller
@RestController
@RequestMapping("/")
public class AwsStatusController {
    @GetMapping("/healthcheck")
    public ResponseEntity<Void> healthcheck(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
