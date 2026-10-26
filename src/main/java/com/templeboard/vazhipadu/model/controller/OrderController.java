package com.templeboard.vazhipadu.model.controller;

import com.templeboard.vazhipadu.model.dto.OrderDto;
import com.templeboard.vazhipadu.model.dto.OrderSummaryDto;
import com.templeboard.vazhipadu.model.enums.OrderStatus;
import com.templeboard.vazhipadu.model.request.OrderRequest;
import com.templeboard.vazhipadu.model.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(
            @Valid @RequestBody OrderRequest request) {
        return orderService.create(request);
    }

    @GetMapping("/{id}")
    public OrderDto findById(@PathVariable Long id) {
        return orderService.findById(id);
    }


    @GetMapping("/search")
    public Page<OrderSummaryDto> searchOrders(
            @RequestParam Long templeId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate bookingDate,
            @RequestParam(required = false)
            OrderStatus status,
            @RequestParam(required = false)
            String phoneNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return orderService.search(
                templeId, bookingDate, status, phoneNumber, page, size
        ).map(order -> new OrderSummaryDto(
                order.getId(),
                order.getTempleId(),
                order.getDevoteeName(),
                order.getPhoneNumber(),
                order.getNakshatra(),
                order.getBookingDate(),
                order.getOrderStatus(),
                order.getTotalAmount()
        ));
    }
}
