package com.mj.modernjava.review.repository;

import com.mj.modernjava.review.domain.Cars;
import org.springframework.data.repository.CrudRepository;

public interface CarsRepository extends CrudRepository<Cars, String> {

}
