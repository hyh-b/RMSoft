package com.example.rmsoft.service;

import com.example.rmsoft.dto.MemberDto;
import com.example.rmsoft.dto.SubscriptionDto;
import com.example.rmsoft.mapper.SubscriptionMapper;
import com.example.rmsoft.security.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.TimeZone;

@RequiredArgsConstructor
@Service
public class SubscriptionService {

    private final SubscriptionMapper subscriptionMapper;

    private final CustomUserDetailService customUserDetailService;

    private final ServiceService serviceService;

    // 구독 신청
    public void createSubscription(SubscriptionDto subscriptionDto) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        // 현재 로그인중인 회원 아이디
        MemberDto memberDto = customUserDetailService.getMemberDto();
        subscriptionDto.setMemberId(memberDto.getMemberId());
        // 서비스 코드 구하기
        String serviceName = subscriptionDto.getServiceType();
        subscriptionDto.setServiceCode(serviceService.getServiceCode(serviceName));
        // 구독 시작일이 오늘이라면 구독상태 'S' 아니라면 'N'
        LocalDate currentDate = LocalDate.now();
        if(currentDate.isEqual(subscriptionDto.getSubscriptionStartDate())) {
            subscriptionDto.setSubscriptionStatus('S');
        } else {
            subscriptionDto.setSubscriptionStatus('N');
        }

        subscriptionMapper.createSubscription(subscriptionDto);
    }

    // 매일 자정에 구독 상태 갱신
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void updateSubscriptionStatus() {
        subscriptionMapper.updateSubscriptionStatus();
    }

}
