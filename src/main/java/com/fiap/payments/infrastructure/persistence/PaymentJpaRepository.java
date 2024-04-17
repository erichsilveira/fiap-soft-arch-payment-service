package com.fiap.payments.infrastructure.persistence;

import com.fiap.payments.infrastructure.model.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentModel, String> {

}
