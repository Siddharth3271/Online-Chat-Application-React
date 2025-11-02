package com.siddh.chat_app_backend.service;

import com.siddh.chat_app_backend.DTO.RoomDTO;
import com.siddh.chat_app_backend.document.Room;
import com.siddh.chat_app_backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final RoomRepository roomRepository;
    //for sending and receiving messages
    public Optional<Room> findByRoomId(String roomName) {
        return roomRepository.findByRoomId(roomName);
    }

    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

}
