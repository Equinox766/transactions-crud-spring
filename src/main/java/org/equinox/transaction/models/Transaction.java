package org.equinox.transaction.models;

import jakarta.persistence.*;
import lombok.Data;
import org.equinox.transaction.enums.Channel;
import org.equinox.transaction.enums.Status;

import java.util.Date;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String reference;
    private String accountIban;
    private Date date;
    private Double amount;
    private Double fee;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Channel channel;
}
