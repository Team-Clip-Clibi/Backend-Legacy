package com.clip.batch.notification.listener;

//
//@Component
//@RequiredArgsConstructor
//@StepScope
//@Slf4j
//public class FcmOnethingWriterListener{
//    private final SendFCMService sendFCMService;
//
////    @AfterWrite
////    public void afterWrite(Chunk<? extends OnethingMatchingProjection> items, @Value("#{jobParameters['messageTemplateType']}") String messageTemplateType) {
////        log.info("FcmOnethingWriterListener" + messageTemplateType);
////        sendFCMService.sendMsg(MessageTemplateType.valueOf(messageTemplateType), "ONE_THING", null);
////    }
//}
//
//public record UserFcmData(
//        Long matchingId,
//        String targetDeviceType,
//        String fcmToken,
//        MessageParams params
//) {
//}
//final MessageTemplateType messageTemplateType, final String matchingType, final Map<Long, FcmNotificationEvent.UserFcmData> userDataMap