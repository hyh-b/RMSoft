package com.example.rmsoft.service;

import com.example.rmsoft.dto.ServiceDto;
import com.example.rmsoft.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ServiceService {

    private final ServiceMapper serviceMapper;
    // 서비스 명과 가격 구하기
    public ServiceDto getServiceTypeAndPrice(String serviceType, String storage) {
        String serviceTypePattern = serviceType+"%";
        return serviceMapper.getServiceTypeAndPrice(serviceTypePattern, storage);
    }
    // 서비스 코드 구하기
    public int getServiceCode(String serviceName) {
        return serviceMapper.getServiceCode(serviceName);
    }
}
