package com.templeboard.vazhipadu.model.service;

import com.templeboard.vazhipadu.model.dto.OrderDto;
import com.templeboard.vazhipadu.model.dto.OrderItemDto;
import com.templeboard.vazhipadu.model.entity.Offering;
import com.templeboard.vazhipadu.model.entity.OrderItem;
import com.templeboard.vazhipadu.model.entity.VazhipaduOrder;
import com.templeboard.vazhipadu.model.exception.BusinessException;
import com.templeboard.vazhipadu.model.repository.OfferingRepository;
import com.templeboard.vazhipadu.model.repository.VazhipaduOrderRepository;
import com.templeboard.vazhipadu.model.request.OrderItemRequest;
import com.templeboard.vazhipadu.model.request.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OfferingRepository offeringRepository;
    private final VazhipaduOrderRepository orderRepository;

    @Transactional
    public OrderDto create(OrderRequest request) {
        VazhipaduOrder order = new VazhipaduOrder();
        order.setDevoteeName(request.getDevoteeName());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setNakshatra(request.getNakshatra());
        order.setBookingDate(request.getBookingDate());
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemDto itemRequest : request.getItems()) {
            Offering offering = offeringRepository
                    .findById(itemRequest.getOfferingId())
                    .orElseThrow(() -> new BusinessException(
                            HttpStatus.NOT_FOUND,
                            "Offering not found: "
                                    + itemRequest.getOfferingId()
                    ));

            if (!offering.isActive()) {
                throw new BusinessException(
                        HttpStatus.BAD_REQUEST,
                        "Offering is inactive: " + offering.getName()
                );
            }

            BigDecimal unitPrice = offering.getPrice();
            BigDecimal subtotal = unitPrice.multiply(
                    BigDecimal.valueOf(itemRequest.getQuantity())
            );

            total = total.add(subtotal);
            OrderItem orderItem = new OrderItem(
                    offering,
                    itemRequest.getQuantity(),
                    unitPrice
            );
            order.addItem(orderItem);
        }
        order.setTotalAmount(total);
        VazhipaduOrder savedOrder = orderRepository.save(order);
        return toResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderDto findById(Long id) {
        VazhipaduOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "Order not found: " + id
                ));
        return toResponse(order);
    }

    private OrderDto toResponse(VazhipaduOrder order) {
        List<OrderItemDto> items = order.getItems()
                .stream()
                .map(item -> {
                    BigDecimal subtotal = item.getUnitPrice()
                            .multiply(BigDecimal.valueOf(
                                    item.getQuantity()
                            ));

                    return new OrderItemDto(
                            item.getOffering().getId(),
                            item.getOffering().getName(),
                            item.getQuantity(),
                            item.getUnitPrice(),
                            subtotal
                    );
                })
                .toList();
        return new OrderDto(
                order.getId(),
                order.getDevoteeName(),
                order.getPhoneNumber(),
                order.getNakshatra(),
                order.getBookingDate(),
                items,
                order.getTotalAmount()
        );
    }
}
