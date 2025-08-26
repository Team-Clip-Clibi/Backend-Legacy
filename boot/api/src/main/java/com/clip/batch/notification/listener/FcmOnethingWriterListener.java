package com.clip.batch.notification.listener;

import com.clip.batch.notification.projection.OnethingMatchingProjection;
import com.clip.infra.fcm.event.FcmNotificationEvent;
import com.clip.infra.fcm.service.SendFCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.item.Chunk;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FcmOnethingWriterListener{
    private final ApplicationEventPublisher sendFCMEventPublisher;

//    @AfterWrite
//    public void afterWrite(Chunk<? extends OnethingMatchingProjection> items) {
//        sendFCMEventPublisher.publishEvent(new FcmNotificationEvent(
//                this,
//                templateType,
//                matchingType,
//                userDataMap));
//    }
}
