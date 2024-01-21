package com.example.rmsoft.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {

    private String memberId;
    private int chatCode;
    private String message;
    private char isRead;


}
