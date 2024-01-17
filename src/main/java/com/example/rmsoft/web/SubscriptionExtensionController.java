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
public class SubscriptionExtensionController {

    private final SubscriptionExtensionService subscriptionExtensionService;

    @PostMapping("/api/subscription/extension")
    public ResponseEntity<String> subscriptionExtension(@RequestBody SubscriptionExtensionDto subscriptionExtensionDto) {
        System.out.println("컨트롤러시작");
        System.out.println("컨트롤러시작스타트데이"+subscriptionExtensionDto.getExtensionStartDate());
        System.out.println("컨트롤러dps엔드데이"+subscriptionExtensionDto.getExtensionEndDate());
        subscriptionExtensionService.subscriptionExtension(subscriptionExtensionDto);
        return ResponseEntity.ok("연장이 완료되었습니다.");
    }
}
