package com.mj.modernjava.review.repository;

import com.mj.modernjava.review.domain.Cars;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarsRepositoryJpa extends JpaRepository<Cars, String> {
}
