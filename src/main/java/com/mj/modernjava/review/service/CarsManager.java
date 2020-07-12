package com.mj.modernjava.review.service;

import com.mj.modernjava.common.CommonCodes;
import com.mj.modernjava.review.domain.Cars;
import com.mj.modernjava.review.domain.vo.DemandVO;
import com.mj.modernjava.review.repository.CarsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;

@Service
public class CarsManager {

    @Autowired
    private CarsRepository carsRepository;

    public Map<String, List<Cars>> recommendedCarsBy(DemandVO vo) {
        return StreamSupport
                .stream(carsRepository.findAll().spliterator(), false)
                .filter(Cars::isFast)
                .filter(car -> car.isIntervenePrice(vo.getMinPrice(), vo.getMaxPrice()))
                .limit(vo.getRecommendLimit())
                .collect(groupingBy(Cars::getModel));
    }

    public Map<String, Long> getSalesTotal() {
         return StreamSupport
                .stream(carsRepository.findAll().spliterator(), false)
                .filter(Cars::isNormal)
                .collect(groupingBy(Cars::getBrand, summingLong(Cars::getPrice)));
    }

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
