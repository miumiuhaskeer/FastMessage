package com.miumiuhaskeer.fastmessage.config;

import com.miumiuhaskeer.fastmessage.model.entity.Message;
import com.miumiuhaskeer.fastmessage.service.SequenceGeneratorService;
import com.miumiuhaskeer.fastmessage.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

@Configuration
@RequiredArgsConstructor
public class MessageModelEventListener extends AbstractMongoEventListener<Message> {

    private final SequenceGeneratorService sequenceGeneratorService;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Message> event) {
        Message message = event.getSource();
        Long id = message.getId();

        if (id == null || id < 1) {
            message.setId(sequenceGeneratorService.next(Message.SEQUENCE_NAME));
            message.setCreationDateTime(DateTimeUtil.currentDateTime());
        }
    }
}
