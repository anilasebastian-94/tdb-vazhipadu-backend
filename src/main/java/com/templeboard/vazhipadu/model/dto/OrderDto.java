package com.templeboard.vazhipadu.model.dto;

import com.templeboard.vazhipadu.model.entity.OrderItem;
import com.templeboard.vazhipadu.model.request.OrderItemRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderDto {
    Long id;
    String devoteeName;
    String phoneNumber;
    String nakshatra;
    LocalDate bookingDate;
    List<OrderItemDto> items;
    BigDecimal totalAmount;

}
