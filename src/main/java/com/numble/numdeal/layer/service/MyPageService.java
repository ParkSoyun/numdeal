package com.numble.numdeal.layer.service;

import com.numble.numdeal.layer.dto.response.SignInResponseDto;
import com.numble.numdeal.layer.dto.response.TimedealResponseDto;
import com.numble.numdeal.layer.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final ProductRepository productRepository;

    // 내가 등록한 타임딜 리스트 가져오기
    public List<TimedealResponseDto> getMyTimedeal(SignInResponseDto memberInfo) {
        return productRepository.findBySellerId(memberInfo.getId());
    }
}
