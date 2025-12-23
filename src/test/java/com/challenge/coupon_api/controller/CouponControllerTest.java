package com.challenge.coupon_api.controller;

import com.challenge.coupon_api.dto.CouponRequestDTO;
import com.challenge.coupon_api.dto.CouponResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CouponControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("POST /api/coupons - deve criar cupom com sucesso")
    void shouldCreateCouponSuccessfully() {
        CouponRequestDTO request = new CouponRequestDTO(
                "SAVE@1!",
                "Cupom válido",
                new BigDecimal("10.00"),
                LocalDate.now().plusDays(1),
                true
        );

        webTestClient.post()
                .uri("/api/coupons")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CouponResponseDTO.class)
                .value(response -> {
                    assertNotNull(response.getId());
                    assertTrue(response.getCode().matches("[A-Z0-9]{6}"));
                });
    }

    @Test
    @DisplayName("POST /api/coupons - deve retornar 400 para data no passado")
    void shouldReturnBadRequestWhenExpirationDateIsPast() {
        CouponRequestDTO request = new CouponRequestDTO(
                "PAST01",
                "Inválido",
                new BigDecimal("10.00"),
                LocalDate.now().minusDays(1),
                false
        );

        webTestClient.post()
                .uri("/api/coupons")
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("DELETE /api/coupons/{id} - não deve permitir deletar duas vezes")
    void shouldNotDeleteAlreadyDeletedCoupon() {
        CouponResponseDTO created =
                webTestClient.post()
                        .uri("/api/coupons")
                        .bodyValue(new CouponRequestDTO(
                                "DEL456",
                                "Duplo delete",
                                new BigDecimal("7.00"),
                                LocalDate.now().plusDays(2),
                                false
                        ))
                        .exchange()
                        .expectBody(CouponResponseDTO.class)
                        .returnResult()
                        .getResponseBody();

        webTestClient.delete()
                .uri("/api/coupons/" + created.getId())
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.delete()
                .uri("/api/coupons/" + created.getId())
                .exchange()
                .expectStatus().isEqualTo(409);
    }
}
