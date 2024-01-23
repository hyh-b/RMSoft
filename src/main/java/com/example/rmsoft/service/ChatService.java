package com.example.rmsoft.service;

import com.example.rmsoft.dto.ChatMessageDto;
import com.example.rmsoft.dto.ChatRoomDto;
import com.example.rmsoft.mapper.ChatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMapper chatMapper;

    public void insertChatMessage(ChatMessageDto chatMessageDto) {

        chatMapper.insertChatMessage(chatMessageDto);
    }

    public int findChatCode(String memberId) {
        return chatMapper.findChatCode(memberId);
    }

    public List<ChatMessageDto> findChatMessage(int chatCode) {
        return chatMapper.findChatMessage(chatCode);
    }

    public void updateMessageReadStatus(char isRead, int messageCode) {
        chatMapper.updateMessageReadStatus(isRead, messageCode);
    }

    public int createChatRoom(String memberId) {
        ChatRoomDto chatRoomDto = new ChatRoomDto();
        chatRoomDto.setMemberId(memberId);
        chatMapper.createChatRoom(chatRoomDto);
        return chatRoomDto.getChatCode();
    }

    public boolean checkChatRoom(String memberId) {
        return chatMapper.checkChatRoom(memberId);
    }

    public List<ChatMessageDto> getChatMessage(int chatCode) {
        return chatMapper.getChatMessage(chatCode);
    }
}
