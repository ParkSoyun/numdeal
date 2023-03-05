package com.numble.numdeal.layer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitHubActionsTestController {
    @GetMapping("/")
    public String test(){
        return "GitHub Actions Test";
    }
}
