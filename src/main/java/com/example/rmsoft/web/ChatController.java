package com.example.rmsoft.web;

import com.example.rmsoft.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
}
