package com.challenge.coupon_api.service;

import com.challenge.coupon_api.dto.CouponRequestDTO;
import com.challenge.coupon_api.dto.CouponResponseDTO;
import com.challenge.coupon_api.repository.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    @DisplayName("Deve criar cupom com sucesso")
    void shouldCreateCouponSuccessfully() {
        CouponRequestDTO request = new CouponRequestDTO(
                "SAVE@1!",
                "Cupom válido",
                new BigDecimal("10.00"),
                LocalDate.now().plusDays(1),
                true
        );

        CouponResponseDTO response = couponService.createCoupon(request);

        assertNotNull(response.getId());
        assertEquals("SAVE1", response.getCode());
        assertTrue(couponRepository.existsByCodeAndDeletedFalse("SAVE1"));
    }

    @Test
    @DisplayName("Deve remover caracteres especiais e garantir código com 6 caracteres")
    void shouldNormalizeCouponCode() {
        CouponRequestDTO request = new CouponRequestDTO(
                "AB@C#12$3",
                "Código normalizado",
                new BigDecimal("5.00"),
                LocalDate.now().plusDays(1),
                false
        );

        CouponResponseDTO response = couponService.createCoupon(request);

        assertEquals("ABC123", response.getCode());
    }

    @Test
    @DisplayName("Deve lançar exceção quando data de expiração estiver no passado")
    void shouldThrowExceptionWhenExpirationDateInPast() {
        CouponRequestDTO request = new CouponRequestDTO(
                "PAST01",
                "Data inválida",
                new BigDecimal("10.00"),
                LocalDate.now().minusDays(1),
                false
        );

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> couponService.createCoupon(request)
        );

        assertEquals("Expiration date cannot be in the past", ex.getMessage());
    }

    @Test
    @DisplayName("Deve realizar soft delete com sucesso")
    void shouldSoftDeleteCoupon() {
        CouponRequestDTO request = new CouponRequestDTO(
                "DEL123",
                "Deletar",
                new BigDecimal("10.00"),
                LocalDate.now().plusDays(1),
                false
        );

        CouponResponseDTO created = couponService.createCoupon(request);

        couponService.deleteCoupon(created.getId());

        assertThrows(
                NoSuchElementException.class,
                () -> couponService.getCouponById(created.getId())
        );
    }

    @Test
    @DisplayName("Não deve permitir deletar cupom já deletado")
    void shouldNotDeleteAlreadyDeletedCoupon() {
        CouponRequestDTO request = new CouponRequestDTO(
                "DEL456",
                "Duplo delete",
                new BigDecimal("10.00"),
                LocalDate.now().plusDays(1),
                false
        );

        CouponResponseDTO created = couponService.createCoupon(request);
        couponService.deleteCoupon(created.getId());

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> couponService.deleteCoupon(created.getId())
        );

        assertEquals("Coupon has already been deleted", ex.getMessage());
    }
}
