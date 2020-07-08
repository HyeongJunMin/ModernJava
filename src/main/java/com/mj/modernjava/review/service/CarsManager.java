package com.mj.modernjava.review.service;

import com.mj.modernjava.common.CommonCodes;
import com.mj.modernjava.review.domain.Cars;
import com.mj.modernjava.review.repository.CarsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CarsManager {

    @Autowired
    private CarsRepository carsRepository;

    public Cars processSoldOut(Cars car) {
        car.setStatus(CommonCodes.CarStatus.SOLDOUT);
        return carsRepository.save(car);
    }

    public Cars processSoldOutWithColorTuning(Cars car, String color) {
        if (Arrays.asList(CommonCodes.Colors.ALL_COLORS).contains(color)) {
            car.setColor(color);
        }
        return processSoldOut(car);
    }
}
