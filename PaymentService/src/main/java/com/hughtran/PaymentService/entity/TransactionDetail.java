package com.hughtran.PaymentService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetail {

    private long id;
    private long orderId;
    private String paymentMode;
    private String referenceNumber;
    private Instant paymentDate;
    private String
}
