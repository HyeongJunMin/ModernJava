package com.mj.modernjava.review.domain;

import com.mj.modernjava.common.CommonCodes;
import com.mj.modernjava.review.repository.CarsRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class SaleCarAndTuning implements SaleCar {

    @Autowired
    private CarsRepository carsRepository;

    public Cars saleCar(String id) {
        Cars car = carsRepository.findById(id).get();
        car.setStatus(null);
        car.setColor(CommonCodes.Colors.BROWN);
        return carsRepository.save(car);
    }

}
