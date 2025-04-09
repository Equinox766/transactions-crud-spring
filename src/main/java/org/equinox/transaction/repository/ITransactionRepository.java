package org.equinox.transaction.repository;

import org.equinox.transaction.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountIban(String accountIban);
}
