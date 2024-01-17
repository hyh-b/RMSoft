package com.example.rmsoft.mapper;

import com.example.rmsoft.dto.SubscriptionExtensionDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SubscriptionExtensionMapper {

    void subscriptionExtension(SubscriptionExtensionDto subscriptionExtensionDto);
}
