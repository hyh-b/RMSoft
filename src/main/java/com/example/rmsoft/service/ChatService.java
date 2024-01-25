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

    // 채팅 메세지 저장
    public void insertChatMessage(ChatMessageDto chatMessageDto) {

        chatMapper.insertChatMessage(chatMessageDto);
    }
    // 채팅 코드 반환
    public int findChatCode(String memberId) {
        return chatMapper.findChatCode(memberId);
    }
    // 채팅 읽음 상태 갱신
    public void updateMessageReadStatus(String memberId) {
        chatMapper.updateMessageReadStatus(memberId);
    }
    // 채팅방 생성
    public int createChatRoom(String memberId) {
        ChatRoomDto chatRoomDto = new ChatRoomDto();
        chatRoomDto.setMemberId(memberId);
        chatMapper.createChatRoom(chatRoomDto);
        return chatRoomDto.getChatCode();
    }
    // 채팅방 생성 여부
    public boolean checkChatRoom(String memberId) {
        return chatMapper.checkChatRoom(memberId);
    }
    // 채팅 메세지 반환
    public List<ChatMessageDto> getChatMessage(int chatCode) {
        return chatMapper.getChatMessage(chatCode);
    }
    // 채팅 목록 반환
    public List<ChatMessageDto> getChatList(String memberId) {
        return chatMapper.getChatList(memberId);
    }
}
