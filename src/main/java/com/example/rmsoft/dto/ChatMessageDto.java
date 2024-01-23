package com.example.rmsoft.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ChatMessageDto {

    private String memberId;
    private int chatCode;
    private String message;
    private char isRead;
    private String writeTime;

}
