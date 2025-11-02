package com.siddh.chat_app_backend.document;

import com.siddh.chat_app_backend.DTO.MessageDTO;
import com.siddh.chat_app_backend.document.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "rooms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    private String id;
    private String roomId;

    List<Message>messages=new ArrayList<>();
}
