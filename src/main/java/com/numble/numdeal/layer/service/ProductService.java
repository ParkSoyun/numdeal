package com.numble.numdeal.layer.service;

import com.numble.numdeal.layer.domain.ProductStatusEnum;
import com.numble.numdeal.layer.domain.Seller;
import com.numble.numdeal.layer.dto.response.ResultResponseDto;
import com.numble.numdeal.layer.dto.response.SignInResponseDto;
import com.numble.numdeal.layer.form.AddTimedealRequestForm;
import com.numble.numdeal.layer.repository.ProductRepository;
import com.numble.numdeal.layer.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    @Value("${numdeal.timedeal.image.src}")
    private String fileDirectory;

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
}
