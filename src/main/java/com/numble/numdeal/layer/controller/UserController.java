package com.numble.numdeal.layer.controller;

import com.numble.numdeal.layer.dto.response.ResultResponseDto;
import com.numble.numdeal.layer.form.UserSignUpRequestForm;
import com.numble.numdeal.layer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // user 회원가입 페이지
    @GetMapping("/signup/user")
    public String userSignUp(Model model) {
        model.addAttribute("signUpRequestDto", new UserSignUpRequestForm());

        return "user-sign-up";
    }

    // ID 중복확인
    @ResponseBody
    @GetMapping("/signup/user/{id}")
    public ResponseEntity<Boolean> checkExistsId(@PathVariable("id") String id) {
        return new ResponseEntity<>(userService.checkExistsId(id), HttpStatus.OK);
    }

    // 회원가입
    @PostMapping("/signup/user")
    public String signUp(@ModelAttribute("signUpRequestDto") @Valid UserSignUpRequestForm userSignUpRequestForm,
                         BindingResult bindingResult,
                         Model model)
    {
        if(bindingResult.hasErrors()) {
            return "user-sign-up";
        }

        try {
            model.addAttribute("result", userService.signUp(userSignUpRequestForm));
        } catch (IllegalStateException e) {
            model.addAttribute("result", new ResultResponseDto(HttpStatus.BAD_REQUEST, e.getMessage()));
        }

        return "user-sign-up";
    }
}
