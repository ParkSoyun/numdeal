package com.numble.numdeal.layer.service;

import com.numble.numdeal.layer.domain.Product;
import com.numble.numdeal.layer.domain.ProductStatusEnum;
import com.numble.numdeal.layer.domain.Seller;
import com.numble.numdeal.layer.dto.response.ResultResponseDto;
import com.numble.numdeal.layer.dto.response.SignInResponseDto;
import com.numble.numdeal.layer.dto.response.TimedealResponseDto;
import com.numble.numdeal.layer.form.AddTimedealRequestForm;
import com.numble.numdeal.layer.repository.ProductRepository;
import com.numble.numdeal.layer.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    @Value("${numdeal.timedeal.image.src}")
    private String fileDirectory;

    private static final int PAGEABLE_SIZE = 10;

    // 새 타임딜 등록
    public ResultResponseDto addTimedeal(SignInResponseDto memberInfo, AddTimedealRequestForm addTimedealRequestForm) throws IOException {
        if(isInvalidDateTime(addTimedealRequestForm.getOpenDateTime(), addTimedealRequestForm.getCloseDateTime())) {
            throw new IllegalArgumentException("종료시간이 오픈시간보다 빠를 수 없습니다.");
        }

        String imageFileName = saveImage(addTimedealRequestForm.getImageFile());

        Seller seller = getSeller(memberInfo);

        productRepository.save(addTimedealRequestForm.toEntity(seller, imageFileName, getStatus(addTimedealRequestForm.getOpenDateTime())));

        return new ResultResponseDto(HttpStatus.OK, "새 타임딜 등록이 완료되었습니다.");
    }

    // 종료시간이 오픈시간보다 빠른지 확인
    private boolean isInvalidDateTime(String strOpenDateTime, String strCloseDateTime) {
        LocalDateTime openDateTime = LocalDateTime.parse(strOpenDateTime);
        LocalDateTime closeDateTime = LocalDateTime.parse(strCloseDateTime);

        return closeDateTime.isBefore(openDateTime);
    }

    // 이미지 저장
    private String saveImage(MultipartFile imageFile) throws IOException {
        if (imageFile.isEmpty()) {
            throw new IllegalArgumentException("첨부한 이미지파일을 다시 확인해주세요.");
        }

        String originName = imageFile.getOriginalFilename();
        String savedName = makeSavedName(originName);
        String savedPath = fileDirectory + savedName;

        try {
            imageFile.transferTo(new File(savedPath));
        } catch (IOException e) {
            throw new IOException("이미지파일 저장에 실패하였습니다.");
        }

        return savedName;
    }

    // 이미지 저장명 생성
    private String makeSavedName(String originName) {
        StringBuilder savedName = new StringBuilder();

        String uuid = UUID.randomUUID().toString();
        String extendsion = originName.substring(originName.lastIndexOf("."));

        return savedName.append(uuid).append(extendsion).toString();
    }

    // seller 가져오기
    private Seller getSeller(SignInResponseDto memberInfo) {
        return sellerRepository.findByEmail(memberInfo.getId())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 접근입니다."));
    }

    // 타임딜 상태 가져오기
    private ProductStatusEnum getStatus(String strOpenDateTime) {
        LocalDateTime openDateTime = LocalDateTime.parse(strOpenDateTime);

        if(openDateTime.isAfter(LocalDateTime.now())) {
            return ProductStatusEnum.TO_DO;
        }

        return ProductStatusEnum.IN_PROCESS;
    }

    // 타임딜 리스트 가져오기
    public Page<TimedealResponseDto> getTimedealList(String status, int page) {
        if(isInvalidFilter(status)) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        Pageable pageable = PageRequest.of(page-1, PAGEABLE_SIZE);

        Page<TimedealResponseDto> timedealResponseDtoPage = productRepository.findByStatus(status, pageable);

        if(isInvalidPage(timedealResponseDtoPage.getTotalPages(), page)) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        return timedealResponseDtoPage;
    }

    // 유효하지 않은 필터인지 확인
    private boolean isInvalidFilter(String status) {
        return !ProductStatusEnum.isExist(status);
    }

    // 존재하지 않는 페이지에 대한 요청인지 확인
    private boolean isInvalidPage(int totalPage, int requestpage) {
        if(requestpage == 1) {
            return false;
        }

        return totalPage < requestpage;
    }

    // 빈 Page<TimedealResponseDto> 리턴
    public Page<TimedealResponseDto> getEmptyPage() {
        return new PageImpl<>(new ArrayList<>(), PageRequest.of(0, PAGEABLE_SIZE), 0);
    }

    // 타임딜 상세 정보 가져오기
    public TimedealResponseDto getTimedealDetail(Long productId) throws IOException {
        return new TimedealResponseDto(getProduct(productId));
    }

    // Product 가져오기
    private Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다."));
    }
}
