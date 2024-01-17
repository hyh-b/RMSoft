package com.example.rmsoft.web;

import com.example.rmsoft.dto.SubscriptionExtensionDto;
import com.example.rmsoft.service.SubscriptionExtensionService;
import com.example.rmsoft.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
public class SubscriptionExtensionController {

    private final SubscriptionExtensionService subscriptionExtensionService;


    @PostMapping("/api/subscription/extension")
    public ResponseEntity<?> subscriptionExtension(@RequestBody SubscriptionExtensionDto subscriptionExtensionDto) {

        subscriptionExtensionService.subscriptionExtension(subscriptionExtensionDto);
        return ResponseEntity.ok("연장이 완료되었습니다.");
    }
}
