package com.example.rmsoft.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DashboardResponseDto {

    private String name;
    private String storage;
    private int price;
    private int subscriptionCode;
    private LocalDate subscriptionEndDate;
}
