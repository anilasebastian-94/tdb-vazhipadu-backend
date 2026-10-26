package com.templeboard.vazhipadu.model.dto;

import com.templeboard.vazhipadu.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class OrderSummaryDto {
    private Long id;
    private Long templeId;
    private String devoteeName;
    private String phoneNumber;
    private String nakshatra;
    private LocalDate bookingDate;
    private OrderStatus status;
    private BigDecimal totalAmount;
}
