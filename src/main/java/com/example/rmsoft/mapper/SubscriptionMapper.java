package com.example.rmsoft.mapper;

import com.example.rmsoft.dto.SubscriptionDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SubscriptionMapper {

    void createSubscription(SubscriptionDto subscriptionDto);

}
