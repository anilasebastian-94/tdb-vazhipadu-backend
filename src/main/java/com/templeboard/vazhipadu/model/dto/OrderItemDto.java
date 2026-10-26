package com.templeboard.vazhipadu.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class OrderItemDto {
   private Long offeringId;
    private String offeringName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}
