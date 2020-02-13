package com.cache.bp.bpcashed.event;

import com.cache.bp.bpcashed.model.Loan;
import org.springframework.context.ApplicationEvent;

public class LoanReceivedEvent extends ApplicationEvent {
    private static final long serialVersionUID = 1L;

    private Loan loan;

    public LoanReceivedEvent(Object source, Loan message) {
        super(source);
        this.loan = message;
    }

    public Loan getMessage() {
        return loan;
    }
}
