package com.numble.numdeal.layer.controller;

import com.numble.numdeal.layer.Constants;
import com.numble.numdeal.layer.form.SignInRequestForm;
import com.numble.numdeal.layer.dto.response.ResultResponseDto;
import com.numble.numdeal.layer.service.SignInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class SignInController {
    private final SignInService signInService;

    // 로그인 페이지
    @GetMapping("/signin")
    public String signInPage(Model model) {
        model.addAttribute("signInRequestForm", new SignInRequestForm());

        return "sign-in";
    }

    // 로그인
    @PostMapping("/signin")
    public String signIn(@ModelAttribute("signInRequestForm") SignInRequestForm signInRequestForm,
                         HttpServletRequest request,
                         Model model)
    {
        HttpSession session = request.getSession();

        try {
            session.setAttribute(Constants.MEMBER_INFO, signInService.signIn(signInRequestForm));
        } catch (IllegalStateException e) {
            model.addAttribute("result", new ResultResponseDto(HttpStatus.BAD_REQUEST, e.getMessage()));

            return "sign-in";
        }

        return "redirect:/timedeal";
    }
}
