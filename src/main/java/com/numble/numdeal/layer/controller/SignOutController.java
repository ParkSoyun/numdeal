package com.numble.numdeal.layer.controller;

import com.numble.numdeal.layer.dto.response.ResultResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class SignOutController {

    // 로그아웃
    @PostMapping("/signout")
    public ResponseEntity<ResultResponseDto> signOut(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        session.invalidate();

        return new ResponseEntity<>(new ResultResponseDto(HttpStatus.OK, "로그아웃이 완료되었습니다."), HttpStatus.OK);
    }
}
