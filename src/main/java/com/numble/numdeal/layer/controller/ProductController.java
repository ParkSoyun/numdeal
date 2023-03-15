package com.numble.numdeal.layer.controller;

import com.numble.numdeal.layer.Constants;
import com.numble.numdeal.layer.domain.ProductStatusEnum;
import com.numble.numdeal.layer.dto.response.ResultResponseDto;
import com.numble.numdeal.layer.dto.response.SignInResponseDto;
import com.numble.numdeal.layer.form.AddTimedealRequestForm;
import com.numble.numdeal.layer.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // 새 타임딜 등록 페이지
    @GetMapping("/timedeal/new")
    public String addTimedealPage(@SessionAttribute(name = Constants.MEMBER_INFO, required = false) SignInResponseDto memberInfo,
                                  Model model)
    {
        model.addAttribute("addTimedealRequestForm", new AddTimedealRequestForm());

        if(memberInfo == null) {
            model.addAttribute("result", new ResultResponseDto(HttpStatus.UNAUTHORIZED, "잘못된 접근입니다."));

            return "add-timedeal";
        }

        if(memberInfo.getAuthority().equals("U")) {
            model.addAttribute("result", new ResultResponseDto(HttpStatus.FORBIDDEN, "잘못된 접근입니다."));

            return "add-timedeal";
        }

        return "add-timedeal";
    }

    // 새 타임딜 등록
    @PostMapping("/timedeal/new")
    public String addTimedeal(@SessionAttribute(name = Constants.MEMBER_INFO, required = false) SignInResponseDto memberInfo,
                              @ModelAttribute("addTimedealRequestForm") @Valid AddTimedealRequestForm addTimedealRequestForm,
                              BindingResult bindingResult,
                              Model model)
    {
        if(memberInfo == null) {
            model.addAttribute("result", new ResultResponseDto(HttpStatus.UNAUTHORIZED, "잘못된 접근입니다."));

            return "add-timedeal";
        }

        if(bindingResult.hasErrors()) {
            return "add-timedeal";
        }

        try {
            model.addAttribute("result", productService.addTimedeal(memberInfo, addTimedealRequestForm));
        } catch (IOException | IllegalArgumentException e) {
            model.addAttribute("result", new ResultResponseDto(HttpStatus.BAD_REQUEST, e.getMessage()));
        } catch (IllegalStateException e) {
            model.addAttribute("result", new ResultResponseDto(HttpStatus.FORBIDDEN, "잘못된 접근입니다."));
        }

        return "add-timedeal";
    }

    // 타임딜 리스트 페이지 (메인 페이지)
    @GetMapping("/timedeal")
    public String timedealListPage(@RequestParam(name = "status", defaultValue = ProductStatusEnum.ProductStatus.IN_PROCESS) String status,
                                   @RequestParam(name = "page", defaultValue = "1") int page,
                                   Model model)
    {
        try {
            model.addAttribute("timedealResponseDtoPage", productService.getTimedealList(status, page));
        } catch (IllegalArgumentException e) {
            model.addAttribute("timedealResponseDtoPage", productService.getEmptyPage());
            model.addAttribute("result", new ResultResponseDto(HttpStatus.BAD_REQUEST, e.getMessage()));
        }

        return "timedeal";
    }

    // 타임딜 상세 페이지
    @GetMapping("/timedeal/{id}")
    public String timedealDetailPage(@SessionAttribute(name = Constants.MEMBER_INFO, required = false) SignInResponseDto memberInfo,
                                     @PathVariable("id") Long id,
                                     Model model)
    {
        try {
            model.addAttribute("timedealResponseDto", productService.getTimedealDetail(id));
        } catch (IOException e) {
            model.addAttribute("result", new ResultResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        } catch (IllegalArgumentException e) {
            model.addAttribute("result", new ResultResponseDto(HttpStatus.BAD_REQUEST, e.getMessage()));
        }

        return "timedeal-detail";
    }
}
