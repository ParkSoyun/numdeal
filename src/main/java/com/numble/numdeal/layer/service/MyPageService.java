package com.numble.numdeal.layer.service;

import com.numble.numdeal.layer.domain.Product;
import com.numble.numdeal.layer.dto.response.BuyerResponseDto;
import com.numble.numdeal.layer.dto.response.SignInResponseDto;
import com.numble.numdeal.layer.dto.response.TimedealResponseDto;
import com.numble.numdeal.layer.repository.OrderRepository;
import com.numble.numdeal.layer.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    // 내가 등록한 타임딜 리스트 가져오기
    public List<TimedealResponseDto> getMyTimedeal(SignInResponseDto memberInfo) {
        return productRepository.findBySellerId(memberInfo.getId());
    }

    // 상품 구매자 리스트 가져오기
    public List<BuyerResponseDto> getBuyerList(Long productId, SignInResponseDto memberInfo) {
        Product product = getProduct(productId);

        if(!hasAuthority(product.getSeller().getSellerId(), memberInfo.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        return orderRepository.findByProductId(productId);
    }

    // Product 가져오기
    private Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다."));
    }

    // 요청자(로그인 되어있는 대상)가 해당 Timedeal을 등록한 사람이 맞는지 확인
    private boolean hasAuthority(Long sellerId, Long loginMemberId) {
        return sellerId.equals(loginMemberId);
    }
}
