package com.hughtran.PaymentService.service;

import com.hughtran.PaymentService.entity.TransactionDetail;
import com.hughtran.PaymentService.model.PaymentRequest;
import com.hughtran.PaymentService.model.PaymentResponse;
import com.hughtran.PaymentService.repository.TransactionDetailRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    private TransactionDetailRepository transactionDetailRepository;



    @Override
    public long doPayment(PaymentRequest paymentRequest) {
        log.info("recording payments details: {}", paymentRequest);

        TransactionDetail transactionDetail
                = TransactionDetail
                .builder()
                .paymentMode(paymentRequest.getPaymentMode().name())
                .paymentStatus("SUCCESS")
                .paymentDate(Instant.now())
                .orderId(paymentRequest.getOrderId())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .amount(paymentRequest.getAmount())
                .build();
        transactionDetailRepository.save(transactionDetail);

        log.info("payments details saved with id: {}", transactionDetail.getId());

        return transactionDetail.getId();
    }

    @Override
    public PaymentResponse getPaymentDetailsByOrderId(long orderId) {

        TransactionDetail transactionDetail = transactionDetailRepository.findByOrderId(orderId);

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .paymentId(transactionDetail.getId())
                .paymentMode(transactionDetail.getPaymentMode())
                .paymentDate(transactionDetail.getPaymentDate())
                .orderId(transactionDetail.getOrderId())
                .status(transactionDetail.getPaymentStatus())
                .amount(transactionDetail.getAmount())
                .build();

        return  paymentResponse;
    }
}
