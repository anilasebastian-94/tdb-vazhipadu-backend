package com.templeboard.vazhipadu.model.request;

import com.templeboard.vazhipadu.model.dto.OrderItemDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class OrderRequest {
    @NotBlank(message = "Devotee name is required")
    @Size(max = 100)
   private String devoteeName;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9]{10,15}$",
            message = "Phone number must contain 10 to 15 digits"
    )
   private String phoneNumber;

    @NotBlank(message = "Nakshatra is required")
    @Size(max = 50)
   private String nakshatra;

    @NotNull(message = "Booking date is required")
    @FutureOrPresent(message = "Booking date cannot be in the past")
   private LocalDate bookingDate;

    @NotEmpty(message = "At least one offering is required")
    List<@Valid OrderItemDto> items;

    @NotNull(message = "Temple ID is required")
   private Long templeId;

}
