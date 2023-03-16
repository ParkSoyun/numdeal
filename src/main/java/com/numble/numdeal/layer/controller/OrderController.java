package com.numble.numdeal.layer.controller;

import com.numble.numdeal.layer.Constants;
import com.numble.numdeal.layer.dto.response.ResultResponseDto;
import com.numble.numdeal.layer.dto.response.SignInResponseDto;
import com.numble.numdeal.layer.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // 주문하기
    @PostMapping("/order/{productId}")
    public ResponseEntity<ResultResponseDto> order(@SessionAttribute(name = Constants.MEMBER_INFO, required = false) SignInResponseDto memberInfo,
                                                   @PathVariable("productId") Long productId)
    {
        return new ResponseEntity<>(orderService.order(productId, memberInfo), HttpStatus.OK);
    }
}
