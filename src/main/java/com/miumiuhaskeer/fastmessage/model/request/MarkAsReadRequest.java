package com.miumiuhaskeer.fastmessage.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkAsReadRequest {

    private long chatId;

    @NotBlank
    private String messageId;
}
