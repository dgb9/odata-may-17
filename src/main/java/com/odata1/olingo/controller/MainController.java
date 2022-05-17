package com.odata1.olingo.controller;

import com.odata1.olingo.impl.jpa.data.CrtData;
import com.odata1.olingo.impl.jpa.service.ICrtRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {

    @Autowired
    private ICrtRepo crtRepo;

    @RequestMapping(path="/", method = RequestMethod.GET)
    public String getRootData() {
        List<CrtData> items = crtRepo.findAll();
        StringBuilder b = new StringBuilder();

        items.forEach((item) -> b.append(item.toString()));

        return b.toString();
    }
}
