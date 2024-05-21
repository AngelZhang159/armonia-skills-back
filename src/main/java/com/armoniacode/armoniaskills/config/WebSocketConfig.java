package com.armoniacode.armoniaskills.config;

import com.armoniacode.armoniaskills.service.ChatMessageService;
import com.armoniacode.armoniaskills.service.ChatRoomService;
import com.armoniacode.armoniaskills.util.JWTUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final JWTUtil jwtUtil;

    public WebSocketConfig(ChatMessageService chatMessageService, ChatRoomService chatRoomService, JWTUtil jwtUtil) {
        this.chatMessageService = chatMessageService;
        this.chatRoomService = chatRoomService;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public WebSocketChatHandler webSocketChatHandler() {
        return new WebSocketChatHandler(chatMessageService, chatRoomService, jwtUtil);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketChatHandler(), "/ws").setAllowedOrigins("*");
    }


}
