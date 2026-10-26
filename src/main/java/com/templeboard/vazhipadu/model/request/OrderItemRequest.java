package com.templeboard.vazhipadu.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {
    @NotNull(message = "Offering ID is required")
   private Long offeringId;

    @Positive(message = "Quantity must be greater than zero")
   private int quantity;
}
