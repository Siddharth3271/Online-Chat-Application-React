package com.siddh.chat_app_backend.controller;

import com.siddh.chat_app_backend.DTO.MessageDTO;
import com.siddh.chat_app_backend.DTO.RoomDTO;
import com.siddh.chat_app_backend.document.Message;
import com.siddh.chat_app_backend.document.Room;
import com.siddh.chat_app_backend.payload.MessageRequest;
import com.siddh.chat_app_backend.repository.RoomRepository;
import com.siddh.chat_app_backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Date;

@Controller
@RequiredArgsConstructor
@CrossOrigin("*")
public class ChatController {
    private final ChatService chatService;

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
