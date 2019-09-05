package com.chny.sftp.pool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping(path = "/", produces = "html/text")
    public String index() {
        return "/view/index";
    }

}
