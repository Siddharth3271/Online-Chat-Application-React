package com.siddh.chat_app_backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequestDTO {

    @NotBlank(message = "Room ID cannot be empty")
    @Size(min = 2, max = 50, message = "Room ID must be between 2 and 50 characters")
    private String roomId;

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String userName;

}
