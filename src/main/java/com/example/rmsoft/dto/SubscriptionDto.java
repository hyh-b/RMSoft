package com.example.rmsoft.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SubscriptionDto {

    private int subscriptionCode;
    private String memberId;
    private int serviceCode;
    private String serviceType;
    private int userCount;
    private int subscriptionPeriod;
    private int paymentAmount;
    private LocalDate subscriptionStartDate;
    private LocalDate subscriptionEndDate;
    private String companyName;
    private String phone;
    private String email;
    private String address;
}
