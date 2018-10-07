package com.lanshan.springflutter.controller;

import com.lanshan.springflutter.server.DiscardServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Boot {
    @RequestMapping("flutter")
    public String springBoot() throws Exception{

        return "flutter";
    }
}
