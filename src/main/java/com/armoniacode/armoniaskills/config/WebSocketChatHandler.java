package com.armoniacode.armoniaskills.config;

import com.armoniacode.armoniaskills.entity.ChatMessage;
import com.armoniacode.armoniaskills.dto.ChatMessageDTO;
import com.armoniacode.armoniaskills.entity.ChatRoom;
import com.armoniacode.armoniaskills.service.ChatMessageService;
import com.armoniacode.armoniaskills.service.ChatRoomService;
import com.armoniacode.armoniaskills.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketChatHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketChatHandler.class);

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
        logger.warn("New WebSocket connection CONNECTED: {}", session.getId());

        String token = getTokenFromSession(session);
        if (jwtUtil.validateToken(token)) {
            String userId = jwtUtil.getUUID(token).toString();
            session.getAttributes().put("senderId", userId);
            sessions.put(userId, session);
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
        ChatRoom chatRoom = chatRoomService.findById(UUID.fromString(chatId));
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
                .skillId(chatRoom.getSkill())
                .content(msg)
                .date(new Timestamp(System.currentTimeMillis()))
                .build();

        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        broadcastMessage(savedMsg);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.warn("Old WebSocket connection DISCONNECTED: {}", session.getId());

        String userId = (String) session.getAttributes().get("senderId");
        sessions.remove(userId);
    }


    private void broadcastMessage(ChatMessage chatMessage) throws IOException {
        ChatMessageDTO chatMessageDTO = ChatMessageDTO.fromChatMessage(chatMessage);
        ObjectMapper objectMapper = new ObjectMapper();
        String chatMessageJson = objectMapper.writeValueAsString(chatMessageDTO);
        TextMessage textMessage = new TextMessage(chatMessageJson);

        String recipientId = chatMessage.getReceiver().toString();
        WebSocketSession recipientSession = sessions.get(recipientId);
        if (recipientSession != null && recipientSession.isOpen()) {
            recipientSession.sendMessage(textMessage);
        } else {
            logger.warn("Recipient is not connected");
        }

        String senderId = chatMessage.getSender().toString();
        WebSocketSession senderSession = sessions.get(senderId);
        if (senderSession != null && senderSession.isOpen()) {
            senderSession.sendMessage(textMessage);
        } else {
            logger.warn("Sender is not connected");
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
