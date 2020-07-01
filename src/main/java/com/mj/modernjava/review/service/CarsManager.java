package com.mj.modernjava.review.service;

import com.mj.modernjava.common.CommonCodes;
import com.mj.modernjava.review.domain.Cars;
import com.mj.modernjava.review.repository.CarsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class CarsManager {

    @Autowired
    private CarsRepository carsRepository;

    public Cars processSoldOut(String carnumber) {
        Cars car = carsRepository.findById(carnumber).get();
        car.setStatus(CommonCodes.CarStatus.SOLDOUT);
        return carsRepository.save(car);
    }

    public Cars processSoldOut(String carnumber, String color) {
        Cars car = carsRepository.findById(carnumber).get();
        car.setStatus(CommonCodes.CarStatus.SOLDOUT);
        if (Arrays.asList(CommonCodes.Colors.ALL_COLORS).contains(color)) {
            car.setColor(color);
        }
        return carsRepository.save(car);
    }
}
