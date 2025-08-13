package br.com.gruposupera.rinha.backend.payments.repository;

import br.com.gruposupera.rinha.backend.payments.dto.ProcessorSummaryProjection;
import br.com.gruposupera.rinha.backend.payments.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    @Query("""
        SELECT new br.com.gruposupera.rinha.backend.payments.dto.ProcessorSummaryProjection(
            p.processorType,
            COUNT(p.id),
            SUM(p.amount)
        )
        FROM PaymentEntity p
        WHERE (:from IS NULL OR p.processedAt >= :from)
          AND (:to IS NULL OR p.processedAt <= :to)
        GROUP BY p.processorType
        """)
    List<ProcessorSummaryProjection> getSummary(
            @Param("from") Instant from,
            @Param("to") Instant to
    );
}