package com.siddh.chat_app_backend.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserRegistryService {
    private final Map<String,String>sessionUsers=new ConcurrentHashMap<>();

    private final Map<String, Set<String>>roomUsers=new ConcurrentHashMap<>();

    //when user join the room, adding them to particular user set
    public void addUser(String sessionId,String roomId,String username){
        sessionUsers.put(sessionId,username);
        //create for new room if not exist
        roomUsers.computeIfAbsent(roomId,k->ConcurrentHashMap.newKeySet()).add(username);
    }

    //removal of user from the set
    public String removeUser(String sessionId){
        String username=sessionUsers.remove(sessionId);

        if(username==null){
            return null;
        }

        for(Map.Entry<String,Set<String>>entry: roomUsers.entrySet()){
            String roomId= entry.getKey();
            Set<String>users=entry.getValue();

            if(users.remove(username)){
                if(users.isEmpty()){
                    roomUsers.remove(roomId);
                }
                return roomId;
            }
        }
        return null;
    }

    //getting list of active users for a room
    public Set<String>getUsers(String roomId){
        return roomUsers.getOrDefault(roomId,ConcurrentHashMap.newKeySet());
    }
}
