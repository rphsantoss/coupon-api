package com.challenge.coupon_api.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouponRequestDTO {

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Discount value is required")
    @DecimalMin(value = "0.5", message = "Discount value must be at least 0.5")
    private BigDecimal discountValue;

    @NotNull(message = "Expiration date is required")
    private LocalDate expirationDate;

    @NotNull(message = "Published status must be true or false")
    private Boolean published;
}