package org.equinox.transaction.controllers;

import org.equinox.transaction.enums.Status;
import org.equinox.transaction.models.Transaction;
import org.equinox.transaction.repository.ITransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionRestController {

    @Autowired
    private ITransactionRepository transactionRepository;

    @GetMapping()
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @PostMapping
    public Transaction post(@RequestBody Transaction transaction) {
        // Aplicar lógica de negocio

        double fee = transaction.getFee() != null ? transaction.getFee() : 0.0;
        double netAmount = transaction.getAmount() - fee;

        if (netAmount <= 0) {
            throw new IllegalArgumentException("El monto neto no puede ser 0 o menor.");
        }

        // Deduce la comisión del monto
        transaction.setAmount(netAmount);

        // Asignar estado en base a la fecha
        Date now = new Date();
        if (transaction.getDate().after(now)) {
            transaction.setStatus(Status.PENDIENTE);
        } else {
            transaction.setStatus(Status.LIQUIDADA);
        }

        // Guardar en base de datos
        return transactionRepository.save(transaction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            return new ResponseEntity<>(transaction.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Transaction transaction) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);
        if (transactionOptional.isPresent()) {
            Transaction transactionToUpdate = transactionOptional.get();
            transactionToUpdate.setReference(transaction.getReference());
            transactionToUpdate.setAccountIban(transaction.getAccountIban());
            transactionToUpdate.setDate(transaction.getDate());
            transactionToUpdate.setAmount(transaction.getAmount());
            transactionToUpdate.setFee(transaction.getFee());
            transactionToUpdate.setDescription(transaction.getDescription());
            transactionToUpdate.setStatus(transaction.getStatus());
            transactionToUpdate.setChannel(transaction.getChannel());
            Transaction transactionUpdated = transactionRepository.save(transactionToUpdate);
            return new ResponseEntity<>(transactionUpdated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        transactionRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/account/{iban}")
    public ResponseEntity<List<Transaction>> getByAccount(@PathVariable("iban") String accountIban) {
        List<Transaction> transactions = transactionRepository.findByAccountIban(accountIban);
        if (transactions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/account/{iban}/balance")
    public ResponseEntity<Double> getBalanceByAccount(@PathVariable("iban") String accountIban) {
        List<Transaction> transactions = transactionRepository.findByAccountIban(accountIban);
        if (transactions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        double balance = transactions.stream().mapToDouble(Transaction::getAmount).sum();
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }


}
