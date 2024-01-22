package com.example.rmsoft.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ChatRoomDto {
    private int chatCode;
    private String memberId;

    /*@Builder
    public ChatRoomDto(int chatCode, String member_id) {
        this.chatCode = chatCode;
        this.member_id = member_id;
    }*/
}
