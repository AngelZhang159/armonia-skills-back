package com.armoniacode.armoniaskills.config;

import com.armoniacode.armoniaskills.service.ChatMessageService;
import com.armoniacode.armoniaskills.service.ChatRoomService;
import com.armoniacode.armoniaskills.service.FCMService;
import com.armoniacode.armoniaskills.service.UserService;
import com.armoniacode.armoniaskills.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final FCMService fcmService;
    private final JWTUtil jwtUtil;

    @Bean
    public WebSocketChatHandler webSocketChatHandler() {
        return new WebSocketChatHandler(chatMessageService, chatRoomService, userService, fcmService, jwtUtil);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketChatHandler(), "/ws").setAllowedOrigins("*");
    }


}
