package com.odata1.olingo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @RequestMapping(path="/", method = RequestMethod.GET)
    public String getRootData() {
        return "works fine";
    }
}
