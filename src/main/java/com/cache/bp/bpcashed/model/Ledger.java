package com.cache.bp.bpcashed.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="ms_ledger")
@Builder
@AllArgsConstructor
public class Ledger {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String accountNumber;

    @Column(nullable=false)
    private String creditOrDebit;

    @Column(nullable=false)
    private BigDecimal price;

    @Column(nullable=false)
    private Boolean loanBooked;

    @Column(nullable = false)
    private String url;

    @OneToMany(mappedBy = "ledger", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Loan> loans;

    public Ledger() {
    }

    public Ledger(String url) {
        super();
        this.url = url;
        this.loanBooked=false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCreditOrDebit() {
        return creditOrDebit;
    }

    public void setCreditOrDebit(String creditOrDebit) {
        this.creditOrDebit = creditOrDebit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getLoanBooked() {
        return loanBooked;
    }

    public void setLoanBooked(Boolean loanBooked) {
        this.loanBooked = loanBooked;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }
}
