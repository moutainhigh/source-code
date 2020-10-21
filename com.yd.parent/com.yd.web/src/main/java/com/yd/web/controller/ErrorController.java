package com.yd.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

    /**
     * 404页面
     */
    @RequestMapping(value = "/404.html")
    public String page404() {
        return "404";
    }

    /**
     * 500页面
     */
    @RequestMapping(value = "/500.html")
    public String page500() {
        return "500";
    }
}