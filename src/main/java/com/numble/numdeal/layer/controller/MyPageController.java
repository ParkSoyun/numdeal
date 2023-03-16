package com.numble.numdeal.layer.controller;

import com.numble.numdeal.layer.Constants;
import com.numble.numdeal.layer.dto.response.ResultResponseDto;
import com.numble.numdeal.layer.dto.response.SignInResponseDto;
import com.numble.numdeal.layer.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    // Seller 마이페이지 (등록한 상품 조회 페이지)
    @GetMapping("/mypage/seller")
    public String sellerMyPage(@SessionAttribute(name = Constants.MEMBER_INFO, required = false) SignInResponseDto memberInfo,
                               Model model)
    {
        if(memberInfo == null) {
            model.addAttribute("result", new ResultResponseDto(HttpStatus.UNAUTHORIZED, "잘못된 접근입니다."));

            return "mypage-seller";
        }

        if(memberInfo.getAuthority().equals("U")) {
            model.addAttribute("result", new ResultResponseDto(HttpStatus.FORBIDDEN, "잘못된 접근입니다."));

            return "mypage-seller";
        }

        model.addAttribute("timedealResponseDtoList", myPageService.getMyTimedeal(memberInfo));

        return "mypage-seller";
    }

    // 구매한 사용자 리스트 페이지
    @GetMapping("/order/{productId}")
    public String buyerListPage(@SessionAttribute(name = Constants.MEMBER_INFO, required = false) SignInResponseDto memberInfo,
                                @PathVariable("productId") Long productId,
                                Model model)
    {
        if(memberInfo == null) {
            model.addAttribute("result", new ResultResponseDto(HttpStatus.UNAUTHORIZED, "잘못된 접근입니다."));

            return "mypage-seller";
        }

        if(memberInfo.getAuthority().equals("U")) {
            model.addAttribute("result", new ResultResponseDto(HttpStatus.FORBIDDEN, "잘못된 접근입니다."));

            return "mypage-seller";
        }

        model.addAttribute("buyerResponseDtoList", myPageService.getBuyerList(productId, memberInfo));

        return "buyer-list";
    }
}
