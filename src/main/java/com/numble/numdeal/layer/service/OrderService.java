package com.numble.numdeal.layer.service;

import com.numble.numdeal.layer.Constants;
import com.numble.numdeal.layer.domain.Order;
import com.numble.numdeal.layer.domain.Product;
import com.numble.numdeal.layer.domain.User;
import com.numble.numdeal.layer.dto.response.ResultResponseDto;
import com.numble.numdeal.layer.dto.response.SignInResponseDto;
import com.numble.numdeal.layer.repository.OrderRepository;
import com.numble.numdeal.layer.repository.ProductRepository;
import com.numble.numdeal.layer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    // 주문하기
    @Transactional
    public ResultResponseDto order(Long productId, SignInResponseDto memberInfo) {
        User user = getUser(memberInfo);

        Product product = getProduct(productId);

        if(isSoldOut(product.getStock())) {
            throw new IllegalStateException("재고 수량이 부족하여 주문에 실패하였습니다.");
        }

        if(isClosed(product.getCloseTime())) {
            throw new IllegalStateException("종료된 타임딜입니다.");
        }

        product.reduceStock();

        Order order = Order.builder()
                            .user(user)
                            .product(product)
                            .build();

        orderRepository.save(order);

        return new ResultResponseDto(HttpStatus.OK, "주문이 완료되었습니다.");
    }

    private User getUser(SignInResponseDto memberInfo) {
        if (!memberInfo.getAuthority().equals(Constants.AUTHORITY_USER)) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        return userRepository.findById(memberInfo.getId())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 접근입니다."));
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

    private boolean isSoldOut(int stock) {
        return stock == 0;
    }

    private boolean isClosed(LocalDateTime closeTime) {
        return closeTime.isBefore(LocalDateTime.now());
    }
}
