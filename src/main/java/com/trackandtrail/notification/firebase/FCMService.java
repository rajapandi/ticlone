package com.trackandtrail.notification.firebase;



import com.google.firebase.messaging.*;
import com.trackandtrail.dto.notification.PushNotificationRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FCMService {

    private Logger logger = LoggerFactory.getLogger(FCMService.class);

    public void sendMessage(PushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageWithData(request);
        String response = sendAndGetResponse(message);
        logger.info("Sent message with data. Topic: " + request.getTopic() + ", " + response);
    }

//    public void sendMessageWithoutData(PushNotificationRequest request)
//            throws InterruptedException, ExecutionException {
//        Message message = getPreconfiguredMessageWithoutData(request);
//        String response = sendAndGetResponse(message);
//        logger.info("Sent message without data. Topic: " + request.getTopic() + ", " + response);
//    }
//
//    public void sendMessageToToken(PushNotificationRequest request)
//            throws InterruptedException, ExecutionException {
//        Message message = getPreconfiguredMessageToToken(request);
//        String response = sendAndGetResponse(message);
//        logger.info("Sent message to token. Device token: " + request.getToken() + ", " + response);
//    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder().setSound(NotificationParameter.SOUND.getValue())
                        .setColor(NotificationParameter.COLOR.getValue()).setTag(topic).build()).build();
    }

    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }

//    private Message getPreconfiguredMessageToToken(PushNotificationRequest request) {
//        return getPreconfiguredMessageBuilder(request).setToken(request.getToken())
//                .build();
//    }
//
//    private Message getPreconfiguredMessageWithoutData(PushNotificationRequest request) {
//        return getPreconfiguredMessageBuilder(request).setTopic(request.getTopic())
//                .build();
//    }

    private Message getPreconfiguredMessageWithData(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).putAllData(request.getData()).setTopic(request.getTopic()).setToken(request.getToken())
                .build();
    }

    
    
    private Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequest request) {
        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
        
        Notification notification = Notification
                .builder()
                .setTitle(request.getTitle())
                .setBody(request.getMessage())
                .setImage(request.getImage())                
                .build();
        
        return Message.builder()
                .setApnsConfig(apnsConfig).setAndroidConfig(androidConfig).setNotification(notification);
    }


}
