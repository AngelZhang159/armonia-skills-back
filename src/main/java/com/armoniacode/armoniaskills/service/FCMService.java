package com.armoniacode.armoniaskills.service;

import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FCMService {

    public void sendNotification(String registrationToken, String title, String body) throws FirebaseMessagingException {
        // This registration token comes from the client FCM SDKs.
        // See documentation on defining a message payload.
        Message message = Message.builder()
                .setToken(registrationToken)
                .setAndroidConfig(AndroidConfig.builder()
                        .setTtl(3600 * 1000) // 1 hour in milliseconds
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .setNotification(AndroidNotification.builder()
                                .setTitle(title)
                                .setBody(body)
//                                .setIcon("stock_ticker_update")
//                                .setColor("#f45342")
                                .build())
                        .build())
                .build();

        // Send a message to the device corresponding to the provided
        // registration token.

        log.info("Sending message to token: {}", registrationToken);

        String response = FirebaseMessaging.getInstance().send(message);
        // Response is a message ID string.
        log.info("Successfully sent message: {}", response);
    }
}