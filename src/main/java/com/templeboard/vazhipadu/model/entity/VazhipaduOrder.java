package com.templeboard.vazhipadu.model.entity;

import com.templeboard.vazhipadu.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vazhipadu_order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VazhipaduOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String devoteeName;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column(nullable = false, length = 50)
    private String nakshatra;

    @Column(nullable = false)
    private LocalDate bookingDate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> items = new ArrayList<>();

    @Column(name = "temple_id", nullable = false)
    private Long templeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 20)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
