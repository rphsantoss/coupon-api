package com.challenge.coupon_api.repository;

import com.challenge.coupon_api.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    boolean existsByCodeAndDeletedFalse(String code);

    Optional<Coupon> findByCodeAndDeletedFalse(String code);

    Optional<Coupon> findByIdAndDeletedFalse(Long id);
}