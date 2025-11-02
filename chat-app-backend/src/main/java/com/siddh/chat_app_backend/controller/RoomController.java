package com.siddh.chat_app_backend.controller;

import com.siddh.chat_app_backend.DTO.CreateRoomRequestDTO;
import com.siddh.chat_app_backend.DTO.MessageDTO;
import com.siddh.chat_app_backend.DTO.RoomDTO;

import com.siddh.chat_app_backend.document.Message;
import com.siddh.chat_app_backend.document.Room;
import com.siddh.chat_app_backend.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    //create room
    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@Valid @RequestBody CreateRoomRequestDTO requestDTO){
        Optional<Room> existingRoom = roomService.findByRoomId(requestDTO.getRoomId());
        if (existingRoom.isPresent()) {
            // better to use 409 (Conflict) for already existing resource
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Room '" + requestDTO.getRoomId() + "' already exists");
        }

        RoomDTO roomToCreate = new RoomDTO();
        roomToCreate.setRoomId(requestDTO.getRoomId());
        RoomDTO createdRoom = roomService.createRoom(roomToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    //get room (join)
    @GetMapping("{roomId}")
    public ResponseEntity<?>joinRoom(@PathVariable String roomId){
        try {
            RoomDTO room = roomService.joinRoom(roomId);
            return ResponseEntity.ok(room);
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    //get messages
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<?>getMessages(@PathVariable String roomId,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "20") int size){
        try {
            List<MessageDTO> messages = roomService.getMessages(roomId, page, size);
            return ResponseEntity.ok(messages);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
