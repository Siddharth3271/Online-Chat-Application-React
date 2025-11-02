package com.siddh.chat_app_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {

    private String roomId; // The user-friendly room identifier
    private Integer totalMessages; // A count is useful for pagination metadata

    // The list of messages, using the DTO format, not the Document format
    private List<MessageDTO> messages;
}