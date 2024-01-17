package com.example.rmsoft.service;

import com.example.rmsoft.dto.SubscriptionExtensionDto;
import com.example.rmsoft.mapper.SubscriptionExtensionMapper;
import com.example.rmsoft.mapper.SubscriptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class SubscriptionExtensionService {

    private final SubscriptionExtensionMapper subscriptionExtensionMapper;
    private final SubscriptionMapper subscriptionMapper;
    // 구독 연장
    @Transactional
    public void subscriptionExtension(SubscriptionExtensionDto subscriptionExtensionDto) {
        LocalDate extensionEndDate = subscriptionExtensionDto.getExtensionEndDate();
        int subscriptionCode = subscriptionExtensionDto.getSubscriptionCode();
        // 구독 종료일 갱신
        subscriptionMapper.updateSubscriptionEndDate(extensionEndDate,subscriptionCode);
        // 구독 연장 테이블 데이터 생성
        subscriptionExtensionMapper.subscriptionExtension(subscriptionExtensionDto);
    }
}
