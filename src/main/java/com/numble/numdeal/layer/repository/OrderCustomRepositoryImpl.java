package com.numble.numdeal.layer.repository;

import com.numble.numdeal.layer.domain.QOrder;
import com.numble.numdeal.layer.dto.response.BuyerResponseDto;
import com.numble.numdeal.layer.dto.response.OrderHistoryResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderCustomRepositoryImpl implements OrderCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    QOrder order = QOrder.order;

    @Override
    public List<BuyerResponseDto> findByProductId(Long productId) {
        return jpaQueryFactory.select(Projections.constructor(
                BuyerResponseDto.class,
                order.orderDate,
                order.user.email,
                order.user.name
                ))
                .from(order)
                .where(order.product.productId.eq(productId))
                .orderBy(order.orderDate.desc())
                .fetch();
    }

    @Override
    public List<OrderHistoryResponseDto> findByUserId(Long userId) {
        return jpaQueryFactory.select(Projections.constructor(
                OrderHistoryResponseDto.class,
                order.orderDate,
                order.product.productId,
                order.product.seller.name.as("brandName"),
                order.product.name,
                order.product.salePrice,
                order.product.imageFileName
                ))
                .from(order)
                .where(order.user.userId.eq(userId))
                .orderBy(order.orderDate.desc())
                .fetch();
    }
}
