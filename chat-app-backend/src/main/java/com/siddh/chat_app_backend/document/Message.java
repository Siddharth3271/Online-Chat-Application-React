package com.siddh.chat_app_backend.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "message")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private String sender;
    private String content;
    private Date timeStamp;

}
