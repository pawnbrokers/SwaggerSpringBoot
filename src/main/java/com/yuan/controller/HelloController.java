package com.yuan.controller;


import com.yuan.pojo.User;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/yuan")
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }


    @PostMapping("/user")
    public User user(){
        return new User();
    }


    @PostMapping("/postUser")
    public User user(User user){
        return user;
    }




}
