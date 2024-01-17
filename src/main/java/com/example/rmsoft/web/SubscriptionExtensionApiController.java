package com.example.rmsoft.web;

import com.example.rmsoft.dto.SubscriptionExtensionDto;
import com.example.rmsoft.service.SubscriptionExtensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SubscriptionExtensionApiController {

    private final SubscriptionExtensionService subscriptionExtensionService;

    // 구독 연장 api
    @PostMapping("/api/subscription/extension")
    public ResponseEntity<?> subscriptionExtension(@RequestBody SubscriptionExtensionDto subscriptionExtensionDto) {

        subscriptionExtensionService.subscriptionExtension(subscriptionExtensionDto);
        return ResponseEntity.ok("연장이 완료되었습니다.");
    }
}
