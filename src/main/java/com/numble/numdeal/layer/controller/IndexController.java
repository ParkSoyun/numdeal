package com.numble.numdeal.layer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {

    // 메인 페이지 (타임딜 리스트 페이지)
    @GetMapping("/")
    public String timedealListPage() {

        return "redirect:/timedeal";
    }
}
