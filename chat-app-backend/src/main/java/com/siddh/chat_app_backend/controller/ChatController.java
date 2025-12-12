package com.siddh.chat_app_backend.controller;

import com.siddh.chat_app_backend.DTO.MessageDTO;
import com.siddh.chat_app_backend.DTO.RoomDTO;
import com.siddh.chat_app_backend.document.Message;
import com.siddh.chat_app_backend.document.Room;
import com.siddh.chat_app_backend.payload.MessageRequest;
import com.siddh.chat_app_backend.repository.RoomRepository;
import com.siddh.chat_app_backend.service.ChatService;
import com.siddh.chat_app_backend.service.RoomService;
import com.siddh.chat_app_backend.service.UserRegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@CrossOrigin("*")
public class ChatController {
    private final ChatService chatService;
    private final UserRegistryService userRegistryService;
    private final RoomService roomService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    //handling a new joinee
    @MessageMapping("/chat.join")
    public void joinRoom(@Payload MessageRequest request, StompHeaderAccessor headerAccessor){
        String sessionId=headerAccessor.getSessionId();
        String roomId=request.getRoomId();
        String username=request.getSender();

        //adding user to registry
        userRegistryService.addUser(sessionId,roomId,username);

        //get full updated list
        Set<String> users=userRegistryService.getUsers(roomId);

        //broadcasting the list to that room
        System.out.println("Users joined: "+username+". New User list showing now");
        messagingTemplate.convertAndSend("/topic/users/"+roomId,users);
    }

    //leaving chat
    @MessageMapping("/chat.leave")
    public void leaveRoom(MessageRequest message) {
        roomService.removeUserFromRoom(message.getRoomId(), message.getSender());
    }


    //for sending and receiving message
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public MessageDTO sendMessage(@Payload MessageRequest request, @DestinationVariable String roomId){
        Room room = chatService.findByRoomId(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found: " + request.getRoomId()));

        Message newMessage = Message.builder()
                .sender(request.getSender())
                .content(request.getContent())
                .timeStamp(new Date()) // Set timestamp *now*
                .build();

        room.getMessages().add(newMessage);
        chatService.saveRoom(room);

        return MessageDTO.builder()
                .sender(newMessage.getSender())
                .content(newMessage.getContent())
                .timeStamp(newMessage.getTimeStamp())
                .build();
    }

}
