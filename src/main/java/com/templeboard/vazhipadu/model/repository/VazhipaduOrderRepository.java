package com.templeboard.vazhipadu.model.repository;

import com.templeboard.vazhipadu.model.entity.VazhipaduOrder;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface VazhipaduOrderRepository extends JpaRepository<VazhipaduOrder, Long>, JpaSpecificationExecutor<VazhipaduOrder> {
    @Query(value = """
    SELECT COALESCE(SUM(oi.quantity), 0)
    FROM public.order_item oi
    JOIN public.vazhipadu_order vo
      ON vo.id = oi.order_id
    WHERE oi.offering_id = :offeringId
      AND vo.booking_date = :bookingDate
      AND vo.order_status <> 'CANCELLED'
    """, nativeQuery = true)
    Long countBookedQuantity(Long offeringId, @NotNull(message = "Booking date is required") @FutureOrPresent(message = "Booking date cannot be in the past") LocalDate bookingDate);
}
