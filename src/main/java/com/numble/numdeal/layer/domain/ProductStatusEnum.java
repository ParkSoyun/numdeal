package com.numble.numdeal.layer.domain;

import java.util.Arrays;

public enum ProductStatusEnum {
    TO_DO(ProductStatus.TO_DO),
    IN_PROCESS(ProductStatus.IN_PROCESS),
    CLOSED(ProductStatus.CLOSED);

    public static class ProductStatus {
        public static final String TO_DO = "예정";
        public static final String IN_PROCESS = "진행중";
        public static final String CLOSED = "종료";
    }

    private final String status;

    ProductStatusEnum(String status) {
        this.status = status;
    }

    public static boolean isExist(String status) {
        return Arrays.stream(values())
                .anyMatch(productStatus -> productStatus.status.equals(status));
    }

    public String toString() {
        return status;
    }
}
