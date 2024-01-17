package com.example.rmsoft.mapper;

import com.example.rmsoft.dto.SubscriptionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface SubscriptionMapper {

    void createSubscription(SubscriptionDto subscriptionDto);

    void updateSubscriptionEndDate(
            @Param("extensionEndDate") LocalDate extensionEndDate, @Param("subscriptionCode") int subscriptionCode);
}
