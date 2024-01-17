package com.example.rmsoft.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SubscriptionExtensionDto {

    private int subscriptionCode;
    private int extensionPeriod;
    private int paymentAmount;
    private LocalDate extensionStartDate;
    private LocalDate extensionEndDate;
}
