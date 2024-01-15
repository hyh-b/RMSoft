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
public class ServiceController {

    private final ServiceService serviceService;

    // 서비스명과 가격 반환 api
    @GetMapping("/api/service/type")
    public ResponseEntity<ServiceDto> getServiceTypeAndPrice(@RequestParam String serviceType, @RequestParam String storage){
        System.out.println("zjs컨트롤러시작");
        System.out.println(serviceType);
        System.out.println(storage);
        ServiceDto serviceDto = serviceService.getServiceTypeAndPrice(serviceType, storage);
        System.out.println(serviceDto.getType());
        System.out.println(serviceDto.getPrice());
        return ResponseEntity.ok(serviceDto);
    }
}
