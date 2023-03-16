package com.numble.numdeal.layer.repository;

import com.numble.numdeal.layer.domain.ProductStatusEnum;
import com.numble.numdeal.layer.domain.QProduct;
import com.numble.numdeal.layer.dto.response.TimedealResponseDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    QProduct product = QProduct.product;

    @Override
    public Page<TimedealResponseDto> findByStatus(String status, Pageable pageable) {
        JPQLQuery<TimedealResponseDto> query = jpaQueryFactory.select(Projections.constructor(
                TimedealResponseDto.class,
                    product.productId,
                    product.seller.sellerId,
                    product.seller.name.as("brandName"),
                    product.name,
                    product.regularPrice,
                    product.salePrice,
                    product.stock,
                    product.imageFileName,
                    product.openTime,
                    product.closeTime,
                    product.status
                ))
                .from(product)
                .where(statusEq(status))
                .orderBy(getSortCondition(status))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        QueryResults<TimedealResponseDto> timedealResponseDtoQueryResults = query.fetchResults();

        return new PageImpl<>(timedealResponseDtoQueryResults.getResults(), pageable, timedealResponseDtoQueryResults.getTotal());
    }

    private BooleanExpression statusEq(String status) {
        if(status.equals(ProductStatusEnum.TO_DO.toString())) {
            return product.status.eq(ProductStatusEnum.TO_DO);
        }

        if(status.equals(ProductStatusEnum.CLOSED.toString())) {
            return product.status.eq(ProductStatusEnum.CLOSED);
        }

        return product.status.eq(ProductStatusEnum.IN_PROCESS);
    }

    private OrderSpecifier<?> getSortCondition(String filter) {
        if(filter.equals(ProductStatusEnum.TO_DO.toString())) {
            return product.openTime.asc();
        }

        if(filter.equals(ProductStatusEnum.IN_PROCESS.toString())) {
            return product.closeTime.asc();
        }

        if(filter.equals(ProductStatusEnum.CLOSED.toString())) {
            return product.closeTime.desc();
        }

        throw new IllegalArgumentException("잘못된 접근입니다.");
    }

    @Override
    public List<TimedealResponseDto> findBySellerId(Long sellerId) {
        return jpaQueryFactory.select(Projections.constructor(
                TimedealResponseDto.class,
                product.productId,
                product.seller.sellerId,
                product.seller.name.as("brandName"),
                product.name,
                product.regularPrice,
                product.salePrice,
                product.stock,
                product.imageFileName,
                product.openTime,
                product.closeTime,
                product.status
                ))
                .from(product)
                .where(product.seller.sellerId.eq(sellerId))
                .fetch();
    }
}
