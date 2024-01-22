package com.example.rmsoft.webSocket;

import com.example.rmsoft.dto.ChatMessageDto;
import com.example.rmsoft.dto.ChatRoomDto;
import com.example.rmsoft.service.ChatService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private final ChatService chatService;
    List<HashMap<String, Object>> webSocketSessionList = new ArrayList<>();
    private final ObjectMapper objectMapper;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msg = message.getPayload();
        JsonNode obj = objectMapper.readTree(msg);

        String memberId = obj.get("memberId").asText();
        int chatCode = obj.get("chatCode").asInt();
        String messageText = obj.get("message").asText();

        // 채팅방 찾기
        HashMap<String, Object> chatRoomMap = null;
        for (HashMap<String, Object> map : webSocketSessionList) {
            if (map.get("chatCode").equals(String.valueOf(chatCode))) {
                chatRoomMap = map;
                break;
            }
        }

        char isRead = 'N'; // 기본값 'N'
        if (chatRoomMap != null) {
            // 다른 사용자가 채팅방에 있는지 확인
            for (String key : chatRoomMap.keySet()) {
                if (!key.equals("chatCode") && !key.equals(session.getId())) {
                    isRead = 'Y'; // 다른 사용자가 있으므로 'Y'로 설정
                    break;
                }
            }
        }

        ChatMessageDto chatMessageDto = new ChatMessageDto();
        chatMessageDto.setMemberId(memberId);
        chatMessageDto.setChatCode(chatCode);
        chatMessageDto.setMessage(messageText);
        chatMessageDto.setIsRead(isRead);

        // 채팅 메시지를 데이터베이스에 저장
        chatService.insertChatMessage(chatMessageDto);
        String stringChatCode = obj.get("chatCode").asText();
        HashMap<String, Object> temp = new HashMap<>();
        if (webSocketSessionList.size() > 0) {
            for (int i = 0; i < webSocketSessionList.size(); i++) {
                String sessionChatCode = (String) webSocketSessionList.get(i).get("chatCode");
                if (sessionChatCode.equals(stringChatCode)) {
                    temp = webSocketSessionList.get(i);
                    break;
                }
            }

            for (String k : temp.keySet()) {
                if (k.equals("chatCode")) {
                    continue;
                }

                WebSocketSession wss = (WebSocketSession) temp.get(k);
                if (wss != null) {
                    try {
                        wss.sendMessage(new TextMessage(obj.toString()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //소켓 연결
        super.afterConnectionEstablished(session);

        // URI에서 chatCode 추출
        String url = session.getUri().toString();
        String chatCode = url.split("/ws/chat/")[1]; // URL 패턴에 따라 수정

        // 해당 chatCode가 있는지 확인
        HashMap<String, Object> chatRoomMap = null;
        for (HashMap<String, Object> map : webSocketSessionList) {
            if (map.get("chatCode").equals(chatCode)) {
                chatRoomMap = map;
                break;
            }
        }

        if (chatRoomMap == null) { // 새로운 채팅방
            chatRoomMap = new HashMap<>();
            chatRoomMap.put("chatCode", chatCode);
            webSocketSessionList.add(chatRoomMap);
        }

        // 채팅방에 세션 추가
        chatRoomMap.put(session.getId(), session);

        // 세션 ID를 클라이언트에 전송
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("type", "getId");
        jsonNode.put("sessionId", session.getId());

        String jsonStr = objectMapper.writeValueAsString(jsonNode);
        session.sendMessage(new TextMessage(jsonStr));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

        // 연결 종료된 세션의 ID 추출
        String sessionId = session.getId();

        // webSocketSessionList를 반복하여 종료된 세션을 찾고 제거
        for (HashMap<String, Object> map : webSocketSessionList) {
            if (map.containsKey(sessionId)) {
                map.remove(sessionId);
                break;
            }
        }
    }


}
