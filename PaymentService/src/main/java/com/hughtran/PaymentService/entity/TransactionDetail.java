package com.hughtran.PaymentService.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "TRANSACTION_DETAILS")
public class TransactionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "ORDER_ID")
    private long orderId;
    @Column(name = "MODE")
    private String paymentMode;
    @Column(name = "REFERENCE_NUMBER")
    private String referenceNumber;
    @Column(name = "PAYMENT_DATE")
    private Instant paymentDate;
    @Column(name = "STATUS")
    private String paymentStatus;
    @Column(name = "AMOUNT")
    private long amount;
}
