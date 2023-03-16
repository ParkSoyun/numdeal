package com.numble.numdeal.layer.controller;

import com.numble.numdeal.layer.dto.response.ResultResponseDto;
import com.numble.numdeal.layer.form.SellerSignUpRequestForm;
import com.numble.numdeal.layer.service.SellerService;
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
public class SellerController {
    private final SellerService sellerService;

    // seller 회원가입 페이지
    @GetMapping("/signup/seller")
    public String sellerSignUp(Model model) {
        model.addAttribute("signUpRequestDto", new SellerSignUpRequestForm());

        return "seller-sign-up";
    }

    // ID 중복확인
    @ResponseBody
    @GetMapping("/signup/seller/{id}")
    public ResponseEntity<Boolean> checkExistsId(@PathVariable("id") String id) {
        return new ResponseEntity<>(sellerService.checkExistsId(id), HttpStatus.OK);
    }

    // 회원가입
    @PostMapping("/signup/seller")
    public String signUp(@ModelAttribute("signUpRequestDto") @Valid SellerSignUpRequestForm sellerSignUpRequestForm,
                         BindingResult bindingResult,
                         Model model)
    {
        if(bindingResult.hasErrors()) {
            return "seller-sign-up";
        }

        try {
            model.addAttribute("result", sellerService.signUp(sellerSignUpRequestForm));
        } catch (IllegalStateException e) {
            model.addAttribute("result", new ResultResponseDto(HttpStatus.BAD_REQUEST, e.getMessage()));
        }

        return "seller-sign-up";
    }
}
