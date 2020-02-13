package com.cache.bp.bpcashed.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ms_loan")
public class Loan {

    static final long MESSAGE_TIMEOUT = 24 * 60 * 60 * 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String loanBody;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Timestamp timestamp;

    @ManyToOne(optional = false)
    private Ledger ledger;

    public Loan() {
    }

    public Loan(String loanBody, String contentType, Ledger ledger) {
        super();
        this.loanBody = loanBody;
        this.contentType = contentType;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.ledger = ledger;
    }

    public Long getDestinationId() {
        return ledger.getId();
    }

    public String getDestinationAccountId() {
        return ledger.getAccountNumber();
    }

    public String getDestinationUrl() {
        return ledger.getUrl();
    }

    public Boolean isMessageTimeout() {
        return timestamp.getTime() < System.currentTimeMillis() - MESSAGE_TIMEOUT;
    }

    public static long getMessageTimeout() {
        return MESSAGE_TIMEOUT;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoanBody() {
        return loanBody;
    }

    public void setLoanBody(String loanBody) {
        this.loanBody = loanBody;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Ledger getLedger() {
        return ledger;
    }

    public void setLedger(Ledger ledger) {
        this.ledger = ledger;
    }
}
