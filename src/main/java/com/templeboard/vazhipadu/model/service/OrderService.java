package com.templeboard.vazhipadu.model.service;

import com.templeboard.vazhipadu.model.dto.OrderDto;
import com.templeboard.vazhipadu.model.dto.OrderItemDto;
import com.templeboard.vazhipadu.model.entity.Offering;
import com.templeboard.vazhipadu.model.entity.OrderItem;
import com.templeboard.vazhipadu.model.entity.VazhipaduOrder;
import com.templeboard.vazhipadu.model.enums.OrderStatus;
import com.templeboard.vazhipadu.model.exception.BusinessException;
import com.templeboard.vazhipadu.model.repository.OfferingRepository;
import com.templeboard.vazhipadu.model.repository.VazhipaduOrderRepository;
import com.templeboard.vazhipadu.model.request.OrderItemRequest;
import com.templeboard.vazhipadu.model.request.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
        Map<Long, Integer> requestedQuantities = request.getItems()
                .stream()
                .collect(Collectors.groupingBy(
                        OrderItemDto::getOfferingId,
                        TreeMap::new,
                        Collectors.summingInt(
                                OrderItemDto::getQuantity
                        )
                ));
        Map<Long, Offering> lockedOfferings = new LinkedHashMap<>();
        for (Long offeringId : requestedQuantities.keySet()) {
            Offering offering = offeringRepository
                    .findById(offeringId)
                    .orElseThrow(() -> new BusinessException(
                            HttpStatus.NOT_FOUND,
                            "Offering not found: " + offeringId
                    ));
            if (!offering.isActive()) {
                throw new BusinessException(
                        HttpStatus.BAD_REQUEST,
                        "Offering is inactive: " + offering.getName()
                );
            }
            lockedOfferings.put(offeringId, offering);
        }
        for (Map.Entry<Long, Integer> entry
                : requestedQuantities.entrySet()) {
            Long offeringId = entry.getKey();
            int requestedQuantity = entry.getValue();
            Offering offering = lockedOfferings.get(offeringId);
            Integer dailyLimit = offering.getDailyLimit();
            if (dailyLimit == null) {
                continue;
            }
            long alreadyBooked = orderRepository.countBookedQuantity(
                    offeringId,
                    request.getBookingDate()
            );
            if (alreadyBooked + requestedQuantity > dailyLimit) {
                throw new BusinessException(
                        HttpStatus.CONFLICT,
                        "Daily limit exceeded for offering: "
                                + offering.getName()
                                + ". Limit: " + dailyLimit
                                + ", already booked: " + alreadyBooked
                                + ", requested: " + requestedQuantity
                );
            }
        }
        for (OrderItemDto itemRequest : request.getItems()) {

            Offering offering = lockedOfferings.get(
                    itemRequest.getOfferingId()
            );
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
        order.setTempleId(request.getTempleId());
        order.setOrderStatus(OrderStatus.PENDING);

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
    @Transactional(readOnly = true)
    public Page<VazhipaduOrder> search(
            Long templeId,
            LocalDate bookingDate,
            OrderStatus status,
            String phoneNumber,
            int page,
            int size) {
        if (templeId == null || templeId <= 0) {
            throw new IllegalArgumentException(
                    "A valid templeId is required");
        }
        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page must be zero or greater");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100");
        }
        Specification<VazhipaduOrder> spec =
                (root, query, cb) ->
                        cb.equal(root.get("templeId"), templeId);
        if (bookingDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("bookingDate"), bookingDate));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), status));
        }
        if (phoneNumber != null && !phoneNumber.isBlank()) {
            String phone = phoneNumber.trim();
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("phoneNumber"), phone));
        }
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Order.desc("bookingDate"),
                        Sort.Order.desc("id")
                )
        );
        return orderRepository.findAll(spec, pageable);
    }
}
