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

    @Transactional
    public void subscriptionExtension(SubscriptionExtensionDto subscriptionExtensionDto) {
        LocalDate extensionEndDate = subscriptionExtensionDto.getExtensionEndDate();
        int subscriptionCode = subscriptionExtensionDto.getSubscriptionCode();

        subscriptionMapper.updateSubscriptionEndDate(extensionEndDate,subscriptionCode);
        subscriptionExtensionMapper.subscriptionExtension(subscriptionExtensionDto);
    }
}
