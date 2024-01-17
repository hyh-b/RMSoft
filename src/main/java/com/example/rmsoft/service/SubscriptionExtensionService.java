package com.example.rmsoft.service;

import com.example.rmsoft.dto.SubscriptionExtensionDto;
import com.example.rmsoft.mapper.SubscriptionExtensionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubscriptionExtensionService {

    private final SubscriptionExtensionMapper subscriptionExtensionMapper;

    public void subscriptionExtension(SubscriptionExtensionDto subscriptionExtensionDto) {
        subscriptionExtensionMapper.subscriptionExtension(subscriptionExtensionDto);
    }
}
