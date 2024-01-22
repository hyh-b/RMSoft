package com.example.rmsoft.mapper;

import com.example.rmsoft.dto.ChatMessageDto;
import com.example.rmsoft.dto.ChatRoomDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMapper {
    void insertChatMessage(ChatMessageDto chatMessageDto);

    int findChatCode(String memberId);

    List<ChatMessageDto> findChatMessage(int chatCode);

    void updateMessageReadStatus(@Param("isRead") char isRead, @Param("messageCode") int messageCode);

    void createChatRoom(ChatRoomDto chatRoomDto);

    boolean checkChatRoom(String memberId);
}
