package com.miumiuhaskeer.fastmessage.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {

    private long chatId;

    @NotBlank
    @Size(max = 1024)
    private String content;
}
