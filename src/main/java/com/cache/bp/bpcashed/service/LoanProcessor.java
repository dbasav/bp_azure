package com.cache.bp.bpcashed.service;

import com.cache.bp.bpcashed.event.LoanReceivedEvent;

import com.cache.bp.bpcashed.exception.BpRunTimeException;

import com.cache.bp.bpcashed.model.Ledger;
import com.cache.bp.bpcashed.model.Loan;
import com.cache.bp.bpcashed.repository.LedgerRepository;
import com.cache.bp.bpcashed.repository.LoanRepository;
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
public class LoanProcessor {

    @Autowired
    private LoanRepository messageRepository;

    @Autowired
    private LedgerRepository destinationRepository;

    private final RestTemplate restTemplate;

    public LoanProcessor(RestTemplateBuilder restTemplateBuilder) {

        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Async EventListener for MessageReceivedEvent
     */
    @Async
    @EventListener
    public void loanReceivedListener(LoanReceivedEvent loanReceivedEvent) {
        Loan message = loanReceivedEvent.getMessage();

        log.debug("Listening Event for Laon {}", message.getId());

        processLoansForDestination(message.getLedger());
    }

    /**
     * Scheduled method to process the messages saved on database
     */
    @Scheduled(cron = "0 2 2 * * *") // Run at minute 0 past every 6th hour.
    public void scheduledLaonsProcessor() {
        log.debug("Executing scheduled message processor at {}", new Date(System.currentTimeMillis()));
        System.out.println("Executing scheduled message processor at " + new Date(System.currentTimeMillis()));
        destinationRepository.findAll().forEach(destination -> processLoansForDestination(destination));
    }

    private void processLoansForDestination(Ledger destination) {
        try {
            log.debug("Processing messages for Application {}", destination.getUrl());
            System.out.println("Processing messages for Application " + destination.getUrl());
            destinationRepository.setDestinationOnline(destination.getAccountNumber());

            List<Loan> messages = messageRepository.findAllByLedgerOrderByIdAsc(destination);
            for (Loan message : messages) {
                if (message.isMessageTimeout()) {
                    deleteMessage(message);
                } else {
                    sendMessage(message);
                }
            }
        } catch (BpRunTimeException ex) {
            log.info("processMessagesForDestination caught an exception: {}", ex.getMessage());
            System.out.println("processMessagesForDestination caught an exception:" + ex.getMessage());
        }
    }

    private void sendMessage(Loan message) throws BpRunTimeException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, message.getContentType());
            HttpEntity<String> request = new HttpEntity<>(message.getLoanBody(), headers);

            Thread.sleep(500); // wait 0.5 second before send message

            log.debug("Sending Laon {} to Ledger {}", message.getDestinationAccountId(), message.getDestinationUrl());

            ResponseEntity<String> entity = restTemplate.postForEntity(message.getDestinationUrl(), request, String.class);

            if (entity.getStatusCode().equals(HttpStatus.OK)) {
                onSendMessageSuccess(message);
            } else {
                throw new BpRunTimeException("Non 200 HTTP response code!");
            }

            //Call to external source.

            final String url = "https://webhook.site/fd4e0c37-0136-4d82-b30f-5be24d088d58";

            ResponseEntity<Loan> entity1 = restTemplate.getForEntity(url, Loan.class);

           // ResponseEntity<Loan> entity1 = restTemplate.postForEntity(url, reqeuest,Loan.class);
            ;
            if (entity1.getStatusCode().equals(HttpStatus.OK)) {
                onSendMessageSuccess(message);

            } else {
                throw new BpRunTimeException("Non 200 HTTP response code for EXTERNAL source!");
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

    private void onSendMessageSuccess(Loan message) {
        log.debug("Sent Message {}", message.getId());
        updateLoanMessage(message);
        log.debug("Updated post Sent Message {}", message.getId());

    }

    private void onSendMessageError(Loan message) {
        log.debug("Unsent Message {}", message.getId());
        destinationRepository.setDestinationOffline(message.getDestinationAccountId());
    }

    private void deleteMessage(Loan message) {
        messageRepository.delete(message);
        log.debug("Deleted Message {}", message.getId());
    }

    private void updateLoanMessage(Loan message) {
        //messageRepository.update(message);
        messageRepository.save(message);
        log.debug("Updated Loan Message {}", message.getId());
    }

}
