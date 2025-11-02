package com.siddh.chat_app_backend.repository;

import com.siddh.chat_app_backend.document.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoomRepository extends MongoRepository<Room,String> {

    //get room using room id
    Optional<Room> findByRoomId(String roomId);
}
