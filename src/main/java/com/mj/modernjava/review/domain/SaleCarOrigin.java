package com.mj.modernjava.review.domain;

import com.mj.modernjava.review.repository.CarsRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class SaleCarOrigin implements SaleCar {

    @Autowired
    private CarsRepository carsRepository;

    public Cars saleCar(String id) {
        Cars car = carsRepository.findById(id).get();
        car.setStatus(null);
        return carsRepository.save(car);
    }

}
