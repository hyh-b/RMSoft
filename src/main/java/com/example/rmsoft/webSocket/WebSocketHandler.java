package com.example.rmsoft.webSocket;

import com.example.rmsoft.dto.ChatMessageDto;
import com.example.rmsoft.dto.ChatRoomDto;
import com.example.rmsoft.dto.MemberDto;
import com.example.rmsoft.security.CustomUserDetailService;
import com.example.rmsoft.security.CustomUserDetails;
import com.example.rmsoft.service.ChatService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private final ChatService chatService;

    // 세션정보를 담을 리스트
    List<HashMap<String, Object>> webSocketSessionList = new ArrayList<>();
    private final ObjectMapper objectMapper;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 받은 json데이터 파싱
        String msg = message.getPayload();
        JsonNode obj = objectMapper.readTree(msg);

        String memberId = obj.get("memberId").asText();
        int chatCode = obj.get("chatCode").asInt();
        String messageText = obj.get("message").asText();
        String writeTime = obj.get("writeTime").asText();

        // chatCode값으로 채팅방 찾기
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

        // dto에 저장할 메세지 데이터 세팅
        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .memberId(memberId)
                .chatCode(chatCode)
                .message(messageText)
                .isRead(isRead)
                .writeTime(writeTime)
                .build();

        // 채팅 메시지를 데이터베이스에 저장
        chatService.insertChatMessage(chatMessageDto);

        ObjectNode modifiableObj = (ObjectNode) obj;
        String isReadStr = String.valueOf(isRead);

        // 채팅방에 상대가 존재하는지 확인하기 위한 값
        modifiableObj.put("isRead", isReadStr);

        String stringChatCode = obj.get("chatCode").asText();
        HashMap<String, Object> temp = new HashMap<>();

        // webSocketSessionList에 세션 정보가 있는지 확인
        if (webSocketSessionList.size() > 0) {

            // 세션 정보가 있다면 webSocketSessionList를 순회
            for (int i = 0; i < webSocketSessionList.size(); i++) {

                // 현재 HashMap에서 chatCode 추출
                String sessionChatCode = (String) webSocketSessionList.get(i).get("chatCode");

                // 추출한 chatCode가 현재 메시지의 chatCode와 일치하는지 확인
                if (sessionChatCode.equals(stringChatCode)) {

                    // 일치하는 경우, 해당 HashMap을 temp에 저장하고 반복문 탈출
                    temp = webSocketSessionList.get(i);
                    break;
                }
            }
            // 찾은 채팅방(temp) 내의 모든 세션에 대해 반복
            for (String k : temp.keySet()) {
                // 현재 키가 "chatCode"인 경우, 다음 키로 넘어감 / 다른 키인 세션ID를 찾기 위함
                if (k.equals("chatCode")) {
                    continue;
                }
                // 해당 키에 해당하는 WebSocketSession 객체를 가져옴
                WebSocketSession wss = (WebSocketSession) temp.get(k);
                if (wss != null) {
                    try {
                        // WebSocketSession에 JSON 형태의 문자열 메시지 전송
                        wss.sendMessage(new TextMessage(modifiableObj.toString()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

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

        // 해당 chatCode의 채팅방이 없으면 새로운 채팅방을 생성
        if (chatRoomMap == null) {
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
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(jsonNode)));

        // 채팅방에 있는 다른 참가자들에게 'joined' 메시지 전송
        for (String sessionId : chatRoomMap.keySet()) {
            // 현재 처리 중인 세션과 chatCode 키는 제외(본인 제외)
            if (!sessionId.equals("chatCode") && !sessionId.equals(session.getId())) {
                WebSocketSession participantSession = (WebSocketSession) chatRoomMap.get(sessionId);
                if (participantSession != null) {
                    try {
                        ObjectNode data = objectMapper.createObjectNode();
                        data.put("type", "joined");
                        participantSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(data)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
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
