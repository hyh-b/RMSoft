package com.example.rmsoft.web;

import com.example.rmsoft.dto.ServiceDto;
import com.example.rmsoft.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ServiceApiController {

    private final ServiceService serviceService;

    // 서비스명과 가격 반환 api
    @GetMapping("/api/service/price")
    public ResponseEntity<ServiceDto> getServiceTypeAndPrice(@RequestParam String serviceType, @RequestParam String storage){

        ServiceDto serviceDto = serviceService.getServiceTypeAndPrice(serviceType, storage);
        return ResponseEntity.ok(serviceDto);
    }
}
