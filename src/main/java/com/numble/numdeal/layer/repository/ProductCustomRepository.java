package com.numble.numdeal.layer.repository;

import com.numble.numdeal.layer.dto.response.TimedealResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductCustomRepository {
    Page<TimedealResponseDto> findByStatus(String filter, Pageable pageable);
}
