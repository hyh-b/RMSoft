package com.example.rmsoft.service;

import com.example.rmsoft.dto.MemberDto;
import com.example.rmsoft.dto.SubscriptionDto;
import com.example.rmsoft.mapper.SubscriptionMapper;
import com.example.rmsoft.security.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class SubscriptionService {

    private final SubscriptionMapper subscriptionMapper;

    private final CustomUserDetailService customUserDetailService;

    private final ServiceService serviceService;

    public void createSubscription(SubscriptionDto subscriptionDto) {
        MemberDto memberDto = customUserDetailService.getMemberDto();
        subscriptionDto.setMemberId(memberDto.getMemberId());

        String serviceName = subscriptionDto.getServiceType();
        subscriptionDto.setServiceCode(serviceService.getServiceCode(serviceName));

        LocalDate currentDate = LocalDate.now();
        if(currentDate.isEqual(subscriptionDto.getSubscriptionStartDate())) {
            subscriptionDto.setSubscriptionStatus('S');
        } else {
            subscriptionDto.setSubscriptionStatus('N');
        }

        subscriptionMapper.createSubscription(subscriptionDto);
    }

}
