package com.example.rmsoft.webSocket;

import com.example.rmsoft.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private final ChatService chatService;
    HashMap<String, WebSocketSession> sessionMap = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 메시지 발송
        String msg = message.getPayload();
        ObjectNode obj = jsonToObjectParser(msg);
        for (String key : sessionMap.keySet()) {
            WebSocketSession wss = sessionMap.get(key);
            try {
                wss.sendMessage(new TextMessage(obj.toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 소켓 연결
        super.afterConnectionEstablished(session);
        sessionMap.put(session.getId(), session);
        ObjectNode obj = objectMapper.createObjectNode();
        obj.put("type", "getId");
        obj.put("sessionId", session.getId());
        session.sendMessage(new TextMessage(obj.toString()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 소켓 종료
        sessionMap.remove(session.getId());
        super.afterConnectionClosed(session, status);
    }

    private static ObjectNode jsonToObjectParser(String jsonStr) {
        try {
            return new ObjectMapper().readValue(jsonStr, ObjectNode.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
