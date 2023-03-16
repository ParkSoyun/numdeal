package com.numble.numdeal.layer.repository;

import com.numble.numdeal.layer.dto.response.BuyerResponseDto;
import com.numble.numdeal.layer.dto.response.OrderHistoryResponseDto;

import java.util.List;

public interface OrderCustomRepository {
    List<BuyerResponseDto> findByProductId(Long productId);
    List<OrderHistoryResponseDto> findByUserId(Long userId);
}
