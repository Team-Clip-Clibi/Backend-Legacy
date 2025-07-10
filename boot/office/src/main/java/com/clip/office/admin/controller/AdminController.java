package com.clip.office.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/office/admin")
public class AdminController {


    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "testaslkdjflaskdj";
    }

    @PostMapping("/test1")
    @ResponseBody
    public String test1() {
        return "111111111111111";
    }
}
