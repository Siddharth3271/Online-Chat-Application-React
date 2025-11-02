package com.siddh.chat_app_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//routing message to server
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    //helps in routing messages
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){

        //message will published by server ending with /topic prefix
        config.enableSimpleBroker("/topic");
        //When your frontend sends a message to the backend—say, to actually post a new chat message—that message's destination must begin with /app
        config.setApplicationDestinationPrefixes("/app");
    }

    //STOMP Protocol : text oriented messaging protocol that websocket follow to ease communication b/w clients
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/chat")
                .setAllowedOriginPatterns("http://localhost:5173")
                .withSockJS();
    }

    //establish connection at /chat endpoint
}
