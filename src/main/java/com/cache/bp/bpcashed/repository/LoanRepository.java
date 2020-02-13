package com.cache.bp.bpcashed.repository;


import com.cache.bp.bpcashed.model.Ledger;
import com.cache.bp.bpcashed.model.Loan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LoanRepository extends CrudRepository<Loan, Long> {

    List<Loan> findAllByLedgerOrderByIdAsc(Ledger ledger);

}
