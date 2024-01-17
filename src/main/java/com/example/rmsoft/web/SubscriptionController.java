package com.example.rmsoft.web;

import com.example.rmsoft.dto.SubscriptionDto;
import com.example.rmsoft.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/api/subscription")
    public ResponseEntity<String> createSubscription(@RequestBody SubscriptionDto subscriptionDto) {
        subscriptionService.createSubscription(subscriptionDto);
        return ResponseEntity.ok("서비스 구독 신청이 완료되었습니다.");
    }
}
