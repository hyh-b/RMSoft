package com.example.rmsoft.mapper;

import com.example.rmsoft.dto.ChatMessageDto;
import com.example.rmsoft.dto.ChatRoomDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ChatMapper {
    void insertChatMessage(ChatMessageDto chatMessageDto);

    int findChatCode(String memberId);

    void updateMessageReadStatus(String memberId);

    void createChatRoom(ChatRoomDto chatRoomDto);

    boolean checkChatRoom(String memberId);

    List<ChatMessageDto> getChatMessage(int chatCode);

    List<ChatMessageDto> getChatList(String memberId);
}
