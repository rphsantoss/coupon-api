package com.challenge.coupon_api.dto;

import com.challenge.coupon_api.model.Coupon;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CouponResponseDTO {

    private Long id;
    private String code;
    private String description;
    private BigDecimal discountValue;
    private LocalDate expirationDate;
    private Boolean published;
    private LocalDateTime createdAt;

    public CouponResponseDTO(Coupon coupon) {
        this.id = coupon.getId();
        this.code = coupon.getCode();
        this.description = coupon.getDescription();
        this.discountValue = coupon.getDiscountValue();
        this.expirationDate = coupon.getExpirationDate();
        this.published = coupon.getPublished();
    }
}