package com.templeboard.vazhipadu.model.dto;

import com.templeboard.vazhipadu.model.entity.Offering;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OfferingDto {
    private Long id;

    private String name;

    private BigDecimal price;

    private Boolean active;

    public Offering toEntity() {
        Offering offering = new Offering();
        offering.setName(this.name);
        offering.setPrice(this.price);
        offering.setActive(this.active != null ? this.active : true);
        return offering;
    }
}
