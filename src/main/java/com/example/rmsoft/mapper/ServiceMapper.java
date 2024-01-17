package com.example.rmsoft.mapper;

import com.example.rmsoft.dto.ServiceDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ServiceMapper {
    ServiceDto getServiceTypeAndPrice(@Param("serviceType") String serviceType, @Param("storage") String storage);

    int getServiceCode(String serviceName);
}
