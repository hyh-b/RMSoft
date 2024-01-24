package com.example.rmsoft.web;

import com.example.rmsoft.dto.ChatMessageDto;
import com.example.rmsoft.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatApiController {
    private final ChatService chatService;

    @GetMapping("/api/chat/check")
    public ResponseEntity<?> checkChatRoom(@RequestParam String memberId) {
        try {
            boolean exists = chatService.checkChatRoom(memberId);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("에러 발생");
        }
    }

    @PostMapping("/api/chat/room")
    public ResponseEntity<?> createChatRoom(@RequestParam String memberId) {
        try {
            int chatCode = chatService.createChatRoom(memberId);
            return ResponseEntity.ok(chatCode);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("채팅방 생성 실패");
        }
    }

    @GetMapping("/api/chat/code")
    public ResponseEntity<?> findChatCode(@RequestParam String memberId) {
        try {
            int chatCode = chatService.findChatCode(memberId);
            return ResponseEntity.ok(chatCode);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("chatCode를 찾을 수 없음");
        }
    }

    @GetMapping("/api/chat/message")
    public ResponseEntity<?> getChatMessage(@RequestParam int chatCode) {
        try {
            List<ChatMessageDto> messageList = chatService.getChatMessage(chatCode);
            return ResponseEntity.ok(messageList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메세지가 없습니다.");
        }
    }
}
