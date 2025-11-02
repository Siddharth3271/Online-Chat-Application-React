package com.siddh.chat_app_backend.service;

import com.siddh.chat_app_backend.DTO.MessageDTO;
import com.siddh.chat_app_backend.DTO.RoomDTO;
import com.siddh.chat_app_backend.document.Message;
import com.siddh.chat_app_backend.document.Room;
import com.siddh.chat_app_backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    //create room
    public RoomDTO createRoom(RoomDTO roomDTO){

        System.out.println("--- 1. INSIDE createRoom method. Attempting to create room: " + roomDTO.getRoomId() + " ---");
        Room room = Room.builder()
                .roomId(roomDTO.getRoomId())
                .messages(Collections.emptyList())
                .build();

        System.out.println("--- 2. Room object built. Attempting to save... ---");
        try {
            Room savedRoom = roomRepository.save(room);

            System.out.println("--- 3. SUCCESS! Saved room with Database ID: " + savedRoom.getId() + " ---");

            return convertToDTO(savedRoom);

        } catch (Exception e) {

            System.out.println("--- 4. CATCH BLOCK: FAILED TO SAVE ROOM! ---");
            e.printStackTrace(); // This will print the full error

            // Re-throw the exception so the controller knows it failed
            throw new RuntimeException("Could not save room: " + e.getMessage(), e);
        }
    }

    public Optional<Room> findByRoomId(String roomName) {
        return roomRepository.findByRoomId(roomName);
    }

    //get room (join)
    public RoomDTO joinRoom(String roomId){
        Room room=roomRepository.findByRoomId(roomId)
                .orElseThrow(()->new RuntimeException("Room not found with id: " + roomId));;

        return convertToDTO(room);
    }

    //get messages
    public List<MessageDTO>  getMessages(String roomId,int page,int size){
        Room room=roomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));

        List<Message>messages=room.getMessages();
        if (messages==null || messages.isEmpty()) {
            return Collections.emptyList();
        }

        //Calculate start and end indexes for pagination
        int totalMessages = messages.size();
        int start=Math.max(messages.size()-(page+1)*size, 0);
        int end=messages.size()-(page*size);

        //Handle edge cases (like page > total pages)
        if (start>=end) {
            return Collections.emptyList();
        }

        List<Message> paginatedMessages=messages.subList(start,end);


        return paginatedMessages.stream()
                .map(this::convertMessageToDTO)
                .collect(Collectors.toList());
    }

    private RoomDTO convertToDTO(Room room) {
        List<MessageDTO> messageDTOs=room.getMessages() == null ?
                Collections.emptyList() :
                room.getMessages().stream().map(this::convertMessageToDTO).collect(Collectors.toList());

        return RoomDTO.builder()
                .roomId(room.getRoomId())
                .totalMessages(messageDTOs.size())
                .messages(messageDTOs)
                .build();
    }

    private MessageDTO convertMessageToDTO(Message message) {
        return MessageDTO.builder()
                .sender(message.getSender())
                .content(message.getContent())
                .timeStamp(message.getTimeStamp())
                .build();
    }

}


