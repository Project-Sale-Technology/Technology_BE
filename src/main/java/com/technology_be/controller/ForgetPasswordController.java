package com.technology_be.controller;

import com.technology_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping(value = "/account")
public class ForgetPasswordController {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserService userService;

//    @RequestMapping(value = "/forgor-password" , method = RequestMethod.POST)
//    public ResponseEntity<>
}
