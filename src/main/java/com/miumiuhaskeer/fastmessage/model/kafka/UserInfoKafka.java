package com.miumiuhaskeer.fastmessage.model.kafka;

import com.miumiuhaskeer.fastmessage.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoKafka {

    private String email;
    private Integer messageCount;
    private Integer chatCount;
    private LocalDateTime creationDateTime;

    public UserInfoKafka(User user) {
        this.email = user.getEmail();
        this.messageCount = 0;
        this.chatCount = 0;
        this.creationDateTime = user.getCreationDateTime();
    }
}
