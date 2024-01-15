package com.example.rmsoft.service;

import com.example.rmsoft.dto.ServiceDto;
import com.example.rmsoft.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ServiceService {

    private final ServiceMapper serviceMapper;

    public ServiceDto getServiceTypeAndPrice(String serviceType, String storage) {
        String serviceTypePattern = serviceType+"%";
        return serviceMapper.getServiceTypeAndPrice(serviceTypePattern, storage);
    }
}
