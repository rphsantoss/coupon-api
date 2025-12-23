package com.challenge.coupon_api.service;

import com.challenge.coupon_api.dto.CouponRequestDTO;
import com.challenge.coupon_api.dto.CouponResponseDTO;
import com.challenge.coupon_api.model.Coupon;
import com.challenge.coupon_api.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Transactional
    public CouponResponseDTO createCoupon(CouponRequestDTO request) {
        if (request.getExpirationDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expiration date cannot be in the past");
        }

        String processedCode = processCode(request.getCode());

        if (couponRepository.existsByCodeAndDeletedFalse(processedCode)) {
            throw new IllegalArgumentException("Coupon code already exists");
        }

        Coupon coupon = new Coupon(
            processedCode,
            request.getDescription(),
            request.getDiscountValue(),
            request.getExpirationDate(),
            request.getPublished()
        );

        Coupon savedCoupon = couponRepository.save(coupon);
        return new CouponResponseDTO(savedCoupon);
    }

    @Transactional
    public void deleteCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Coupon not found with id: " + id));

        if (coupon.getDeleted()) {
            throw new IllegalStateException("Coupon has already been deleted");
        }

        coupon.setDeleted(true);
    }

    public CouponResponseDTO getCouponById(Long id) {
        return couponRepository.findByIdAndDeletedFalse(id)
                .map(CouponResponseDTO::new)
                .orElseThrow(() -> new NoSuchElementException("Coupon not found with id: " + id));
    }

    public CouponResponseDTO getCouponByCode(String code) {
        String processedCode = processCode(code);

        return couponRepository.findByCodeAndDeletedFalse(processedCode)
                .map(CouponResponseDTO::new)
                .orElseThrow(() -> new NoSuchElementException("Coupon not found with code: " + processedCode));
    }

    private String processCode(String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Code cannot be empty");
        }

        String cleanCode = code.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();

        if (cleanCode.length() != 6) {
            throw new IllegalArgumentException("Code must have at least 6 alphanumeric characters");
        }

        return cleanCode;
    }
}