package com.ujazdowski.springbootwebflow.web.controller;

import org.springframework.stereotype.Controller;

import java.io.Serializable;

@Controller
public class HomePageController implements Serializable {
    public void helloMessage() {
        System.out.println("Hello, World!");
    }
}
