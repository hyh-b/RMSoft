package com.example.rmsoft.mapper;

import com.example.rmsoft.dto.SubscriptionStatusDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SubscriptionStatusMapper {

    void createSubscriptionStatus(SubscriptionStatusDto subscriptionStatusDto);
}
