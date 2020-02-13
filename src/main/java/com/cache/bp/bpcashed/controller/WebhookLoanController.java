package com.cache.bp.bpcashed.controller;


import com.cache.bp.bpcashed.event.LoanReceivedEvent;
import com.cache.bp.bpcashed.model.Ledger;
import com.cache.bp.bpcashed.model.Loan;
import com.cache.bp.bpcashed.repository.LedgerRepository;
import com.cache.bp.bpcashed.repository.LoanRepository;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/ledgers")
@Slf4j
public class WebhookLoanController implements ApplicationEventPublisherAware {
    @Autowired
    private LedgerRepository ledgerRepository;

    @Autowired
    private LoanRepository loanRepository;

    // Event publisher
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * Register a new application (URL) returning its id
     */

    @PostMapping
    @ApiOperation(value = "Register new Ledger")
    public Long registerNewApplication( @RequestBody Ledger body,@RequestParam(required = true) String account_number) {

        //Ledger applicationRequest = Ledger.builder().name(name).url(url).loanBooked(false).accountNumber(account_number).creditOrDebit("C").price(new BigDecimal(2.0)).build();

        body.setAccountNumber(account_number);
        ledgerRepository.save(body);
        log.debug("Received Ledger {}", body.getUrl());

        return body.getId();
    }

    /**
     * List registered Ledgers [{id, URL},...]
     */
    @ApiOperation(value = "List Ledgers")
    @GetMapping
    public Iterable<Ledger> listApplications() {
        log.debug("Listing Ledger");
        return ledgerRepository.findAll();
    }

    /**
     * Delete a ledger by id
     */
    @ApiOperation(value = "Delete Ledger by id")
    @DeleteMapping("/{id}")
    public void deleteApplication(@PathVariable("id") Long id) {
        Ledger ledger = getApplication(id);
        ledgerRepository.delete(ledger);
        log.debug("Deleted Application {}", ledger.getUrl());
    }

    @ApiOperation(value = "Retrieve Ledger by id")
    @PostMapping("/{accountNumber}")
    public ResponseEntity<Ledger> getLedgerByAccountNumber(@PathVariable("accountNumber") String accountNumber) {

        Ledger ledger = getLedger(accountNumber);
        ledgerRepository.findByAccountNumber(accountNumber);
        log.debug("Retrieve Ledger {}", ledger.getUrl());

        return ResponseEntity.ok().body(ledger);
    }


    /**
     * POST a loan to this ledger
     */
    @ApiOperation(value = "Post Loan to Ledger")
    @PostMapping("/{accountNumber}/loan")
    public void postMessageToApplication(@PathVariable("accountNumber") String accountNumber,
                                         @RequestBody String body,
                                         @RequestHeader("Content-Type") String contentType) {

        validateParam(body, "body");
       // Ledger application = getApplication(id);
        Ledger application = getLedger(accountNumber);
        Loan message = loanRepository.save(new Loan(body, contentType, application));
        log.debug("Received Loan {} for Ledger {}", message.getId(), message.getLedger());

        // Publishes the received message's event ***A WebHOOK ***
        applicationEventPublisher.publishEvent(new LoanReceivedEvent(this, message));
    }

    // Register event publisher
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    private Ledger getApplication(Long id) throws NoSuchElementException {
        Ledger application = ledgerRepository.findById(id).get();
        if (application == null) {
            throw new NoSuchElementException("Does not exist application with ID " + id);
        }
        return application;
    }

    private Ledger getLedger(String accountNumber) throws NoSuchElementException {
        Ledger application = ledgerRepository.findByAccountNumber(accountNumber);
        if (application == null) {
            throw new NoSuchElementException("Does not exist application with ID " + accountNumber);
        }
        return application;
    }

    private void validateParam(String param, String paramName) throws IllegalArgumentException {
        if (param == null || param.isEmpty()) {
            throw new IllegalArgumentException("The '" + paramName + "' must not be null or empty");
        }
    }
}
