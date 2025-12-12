package com.siddh.chat_app_backend.config;

import com.siddh.chat_app_backend.service.UserRegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final UserRegistryService userRegistryService;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        String sessionId= event.getSessionId();
        //remove the user from the registry and get the room
        String roomId= userRegistryService.removeUser(sessionId);

        //if the user was in the room
        if(roomId!=null){
            Set<String>users=userRegistryService.getUsers(roomId);

            //broadcast the list to the room
            System.out.println("User Disconnected: "+sessionId+", New user list is now broadcasting");
            messagingTemplate.convertAndSend("/topic/users/"+roomId,users);
        }
    }
}
