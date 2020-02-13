package com.cache.bp.bpcashed.service;

import com.cache.bp.bpcashed.event.MessageReceivedEvent;
import com.cache.bp.bpcashed.exception.BpRunTimeException;
import com.cache.bp.bpcashed.model.Application;
import com.cache.bp.bpcashed.model.Message;
import com.cache.bp.bpcashed.repository.ApplicationRepository;


import com.cache.bp.bpcashed.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MessageProcessor {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ApplicationRepository destinationRepository;

    private final RestTemplate restTemplate;

    public MessageProcessor(RestTemplateBuilder restTemplateBuilder) {

        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Async EventListener for MessageReceivedEvent
     */
    @Async
    @EventListener
    public void messageReceivedListener(MessageReceivedEvent messageReceivedEvent) {
        Message message = messageReceivedEvent.getMessage();

        log.debug("Listening Event for Message {}", message.getId());

        processMessagesForDestination(message.getApplication());
    }

    /**
     * Scheduled method to process the messages saved on database
     */
    @Scheduled(cron = "0 3 3 * * *") // Run at minute 0 past every 6th hour.
    public void scheduledMessagesProcessor() {
       log.debug("Executing scheduled message processor at {}", new Date(System.currentTimeMillis()));
    System.out.println("Executing scheduled message processor at " +new Date(System.currentTimeMillis()));
        destinationRepository.findAll().forEach(destination -> processMessagesForDestination(destination));
    }

    private void processMessagesForDestination(Application destination) {
        try {
            log.debug("Processing messages for Application {}", destination.getUrl());
            System.out.println("Processing messages for Application " +destination.getUrl());
            destinationRepository.setDestinationOnline(destination.getId());

            List<Message> messages = messageRepository.findAllByApplicationOrderByIdAsc(destination);
            for (Message message : messages) {
                if (message.isMessageTimeout()) {
                    deleteMessage(message);
                } else {
                    sendMessage(message);
                }
            }
        } catch (BpRunTimeException ex) {
            log.info("processMessagesForDestination caught an exception: {}", ex.getMessage());
            System.out.println("processMessagesForDestination caught an exception:" +ex.getMessage());
        }
    }

    private void sendMessage(Message message) throws BpRunTimeException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, message.getContentType());
            HttpEntity<String> request = new HttpEntity<>(message.getMessageBody(), headers);

            Thread.sleep(50000); // wait 0.5 second before send message

            log.debug("Sending Message {} to Application {}", message.getId(), message.getDestinationUrl());

            ResponseEntity<String> entity = restTemplate.postForEntity(message.getDestinationUrl(), request, String.class);

            if (entity.getStatusCode().equals(HttpStatus.OK)) {
                onSendMessageSuccess(message);
            } else {
                throw new BpRunTimeException("Non 200 HTTP response code!");
            }
        } catch (Exception ex) {
            log.info("sendMessage caught an exception: {}", ex.getMessage());

            onSendMessageError(message);
            try {
                throw new BpRunTimeException(ex.getMessage());
            } catch (BpRunTimeException e) {
                e.printStackTrace();
            }
        }
    }

    private void onSendMessageSuccess(Message message) {
       log.debug("Sent Message {}", message.getId());

        deleteMessage(message);
    }

    private void onSendMessageError(Message message) {
        log.debug("Unsent Message {}", message.getId());

        destinationRepository.setDestinationOffline(message.getDestinationId());
    }

    private void deleteMessage(Message message) {
        messageRepository.delete(message);

        log.debug("Deleted Message {}", message.getId());
    }
}
