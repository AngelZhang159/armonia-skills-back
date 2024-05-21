package com.armoniacode.armoniaskills.config;

import com.armoniacode.armoniaskills.entity.ChatMessage;
import com.armoniacode.armoniaskills.entity.ChatRoom;
import com.armoniacode.armoniaskills.service.ChatMessageService;
import com.armoniacode.armoniaskills.service.ChatRoomService;
import com.armoniacode.armoniaskills.util.JWTUtil;
import org.json.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final JWTUtil jwtUtil;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public WebSocketChatHandler(ChatMessageService chatMessageService, ChatRoomService chatRoomService, JWTUtil jwtUtil) {
        this.chatMessageService = chatMessageService;
        this.chatRoomService = chatRoomService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String token = getTokenFromSession(session);
        if (jwtUtil.validateToken(token)) {
            String userId = jwtUtil.getUUID(token).toString();
            session.getAttributes().put("senderId", userId);
            sessions.put(session.getId(), session);
        } else {
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        JSONObject jsonObject = new JSONObject(payload);
        String chatId = null;
        try {
            chatId = jsonObject.getString("chatId");
        } catch (Exception e) {
        }

        String msg = jsonObject.getString("message");

        String senderId = (String) session.getAttributes().get("senderId");
        ChatRoom chatRoom = chatRoomService.findById(UUID.fromString(senderId));
        String receiverId;
        if (chatRoom.getSenderId().equals(UUID.fromString(senderId))) {
            receiverId = chatRoom.getReceiverId().toString();
        } else {
            receiverId = chatRoom.getSenderId().toString();
        }

        ChatMessage chatMessage = ChatMessage.builder()
                .chatId(UUID.fromString(chatId))
                .sender(UUID.fromString(senderId))
                .receiver(UUID.fromString(receiverId))
                .content(msg)
                .build();

        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        broadcastMessage(savedMsg);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
    }


    private void broadcastMessage(ChatMessage chatMessage) throws IOException {
        TextMessage textMessage = new TextMessage(chatMessage.toString());
        String recipientId = chatMessage.getReceiver().toString();
        WebSocketSession recipientSession = sessions.get(recipientId);
        if (recipientSession != null && recipientSession.isOpen()) {
            recipientSession.sendMessage(textMessage);
        }
    }

    private String getTokenFromSession(WebSocketSession session) {
        List<String> authorizationHeaders = session.getHandshakeHeaders().get("Authorization");
        String token;
        if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
            token = authorizationHeaders.get(0);
            return token.substring(7);
        } else {
            return null;
        }
    }
}