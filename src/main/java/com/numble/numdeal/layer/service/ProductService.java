package com.numble.numdeal.layer.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.numble.numdeal.layer.Constants;
import com.numble.numdeal.layer.domain.Product;
import com.numble.numdeal.layer.domain.ProductStatusEnum;
import com.numble.numdeal.layer.domain.Seller;
import com.numble.numdeal.layer.dto.response.ResultResponseDto;
import com.numble.numdeal.layer.dto.response.SignInResponseDto;
import com.numble.numdeal.layer.dto.response.TimedealResponseDto;
import com.numble.numdeal.layer.form.AddTimedealRequestForm;
import com.numble.numdeal.layer.form.EditTimedealRequestForm;
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
import org.springframework.transaction.annotation.Transactional;
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

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final int PAGEABLE_SIZE = 10;

    // 새 타임딜 등록
    public ResultResponseDto addTimedeal(SignInResponseDto memberInfo, AddTimedealRequestForm addTimedealRequestForm) throws IOException {
        if(isInvalidDateTime(addTimedealRequestForm.getOpenDateTime(), addTimedealRequestForm.getCloseDateTime())) {
            throw new IllegalArgumentException("종료시간이 오픈시간보다 빠를 수 없습니다.");
        }

        String imageFile = saveImage(addTimedealRequestForm.getImageFile());

        Seller seller = getSeller(memberInfo);

        productRepository.save(addTimedealRequestForm.toEntity(seller, imageFile));

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

        String savedName = makeSavedName();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(imageFile.getContentType());
        metadata.setContentLength(imageFile.getSize());

        try {
            amazonS3Client.putObject(bucket, savedName, imageFile.getInputStream(), metadata);
        } catch (IOException e) {
            throw new IOException("이미지파일 저장에 실패하였습니다.");
        }

        return amazonS3Client.getUrl(bucket, savedName).toString();
    }

    // 이미지 저장명 생성
    private String makeSavedName() {
        return UUID.randomUUID().toString();
    }

    // seller 가져오기
    private Seller getSeller(SignInResponseDto memberInfo) {
        if (!memberInfo.getAuthority().equals(Constants.AUTHORITY_SELLER)) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        return sellerRepository.findById(memberInfo.getId())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 접근입니다."));
    }

//    // 타임딜 상태 가져오기
//    private ProductStatusEnum getStatus(String strOpenDateTime) {
//        LocalDateTime openDateTime = LocalDateTime.parse(strOpenDateTime);
//
//        if(openDateTime.isAfter(LocalDateTime.now())) {
//            return ProductStatusEnum.TO_DO;
//        }
//
//        return ProductStatusEnum.IN_PROCESS;
//    }

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

    // Timedeal Info 가져오기
    public EditTimedealRequestForm getTimedealInfo(Long productId, SignInResponseDto memberInfo) {
        Product product = getProduct(productId);

        if(!hasAuthority(product.getSeller().getSellerId(), memberInfo.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        return new EditTimedealRequestForm(product);
    }

    // 요청자(로그인 되어있는 대상)가 해당 Timedeal을 등록한 사람이 맞는지 확인
    private boolean hasAuthority(Long sellerId, Long loginMemberId) {
        return sellerId.equals(loginMemberId);
    }

    // 빈 EditTimedealRequestForm 리턴
    public EditTimedealRequestForm getEmptyEditForm() {
        return new EditTimedealRequestForm();
    }

    // 타임딜 정보 수정
    @Transactional
    public ResultResponseDto editTimedeal(EditTimedealRequestForm editTimedealRequestForm, SignInResponseDto memberInfo) {
        Product product = getProduct(editTimedealRequestForm.getProductId());

        if(!hasAuthority(product.getSeller().getSellerId(), memberInfo.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        product.edit(editTimedealRequestForm);

        return new ResultResponseDto(HttpStatus.OK, "타임딜 정보 수정이 완료되었습니다.");
    }

    // 타임딜 삭제
    public ResultResponseDto deleteTimedeal(Long productId, SignInResponseDto memberInfo) {
        Product product = getProduct(productId);

        if(!hasAuthority(product.getSeller().getSellerId(), memberInfo.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        productRepository.deleteById(productId);

        return new ResultResponseDto(HttpStatus.OK, "타임딜이 삭제되었습니다.");
    }
}
