package com.example.rmsoft.mapper;

import com.example.rmsoft.dto.ChatMessageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMapper {
    void insertChatMessage(ChatMessageDto chatMessageDto);

    int findChatCode(String memberId);

    List<ChatMessageDto> findChatMessage(int chatCode);

    void updateMessageReadStatus(@Param("isRead") char isRead, @Param("messageCode") int messageCode);

    void createChatRoom(String memberId);
}
