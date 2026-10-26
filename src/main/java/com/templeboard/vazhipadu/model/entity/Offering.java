package com.templeboard.vazhipadu.model.entity;

import com.templeboard.vazhipadu.model.dto.OfferingDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "offerings")
@Getter
@Setter
public class Offering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "daily_limit")
    private Integer dailyLimit;

    public OfferingDto toDto() {
        OfferingDto dto = new OfferingDto();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setPrice(this.price);
        dto.setActive(this.active);
        dto.setDailyLimit(this.dailyLimit);
        return dto;
    }
}
