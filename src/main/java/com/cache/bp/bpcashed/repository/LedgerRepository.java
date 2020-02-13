package com.cache.bp.bpcashed.repository;

import com.cache.bp.bpcashed.model.Application;
import com.cache.bp.bpcashed.model.Ledger;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface LedgerRepository extends CrudRepository<Ledger, Long> {

    @Modifying
    @Transactional
    @Query("update Ledger d set d.loanBooked = true where d.accountNumber = :accountNumber")
    int setDestinationOnline(@Param("accountNumber") String accountNumber) ;

    @Modifying
    @Transactional
    @Query("update Ledger d set d.loanBooked = false where d.accountNumber = :accountNumber")
    int setDestinationOffline(@Param("accountNumber") String accountNumber) ;

    Ledger findByAccountNumber(String accountNumber);

}
