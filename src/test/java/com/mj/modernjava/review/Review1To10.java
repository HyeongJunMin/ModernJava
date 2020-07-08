package com.mj.modernjava.review;

import com.mj.modernjava.common.CommonCodes;
import com.mj.modernjava.review.domain.Cars;
import com.mj.modernjava.review.domain.vo.DemandVO;
import com.mj.modernjava.review.repository.CarsRepository;
import com.mj.modernjava.review.repository.CarsRepositoryJpa;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class Review1To10 {


    @Autowired
    private CarsRepository carsRepository;

    @Autowired
    private CarsRepositoryJpa carsJpaRepository;

    @Test
    void carRecommendation() {
        //문제 1
        DemandVO demand = DemandVO.builder()
                .minSpeed(250L).minPrice(250_000_000L).maxPrice(300_000_000L).recommendLimit(3).build();

        Map<String, List<Cars>> carList = Lists.newArrayList(carsRepository.findAll())
                .stream()
                .filter(Cars::isFast)
                .filter(car -> car.isIntervenePrice(demand.getMinPrice(), demand.getMaxPrice()))
                .limit(demand.getRecommendLimit())
                .collect(groupingBy(Cars::getModel));

        log.info("car list size : {}", carList.size());
        carList.forEach((k, v) ->log.info("info : " + k + v));
    }

    @Test
    void calculateSalesTotal() {
        //문제 2
        Map<String, Long> collect = Lists.newArrayList(carsRepository.findAll())
                .stream()
                .filter(Cars::isNormal)
                .collect(groupingBy(Cars::getBrand, summingLong(Cars::getPrice)));
        collect.forEach((k, v) -> {
            log.info("model : {}, salesTotal : {}", k, v);
        });
    }

    @Test
    void parallelPerformance() {
        //문제 3
        Set<String> modelNameSet = Sets.newHashSet(carsRepository.findAll()).stream().map(Cars::getModel).collect(toSet());

        StopWatch stopWatch = new StopWatch();
        IntStream.range(0, 10).forEach(i -> {
            stopWatch.reset();
            stopWatch.start();
            modelNameSet.stream().map(model -> model.toUpperCase());
            stopWatch.stop();
            log.info("{} time : {}", i, stopWatch.getNanoTime());
        });

        stopWatch.reset();
        stopWatch.start();
        modelNameSet.parallelStream().map(model -> model.toUpperCase());
        stopWatch.stop();
        log.info("time : {}", stopWatch.getNanoTime());
    }

    @Test
    @Rollback(false)
    void templateMethodTest() {
        //판매되면 status를 null로 변경
        String carNumber = "00하3929";
        Cars carOrigin = carsJpaRepository.findById(carNumber).get();
        log.info("carOrigin.getStatus() : {}", carOrigin.getStatus());
        Cars car = processSaleCommon(carNumber, (Cars carOrg) -> carOrg.setStatus(null));
        log.info("car.getStatus() : {}", car.getStatus());
    }

    @Test
    void templateMethodTest2() {
        String carNumber = "00하3929";
        Cars carOrigin = carsRepository.findById(carNumber).get();
        System.out.println(carOrigin.getStatus() + ", " + carOrigin.getColor());
        processSaleCommon(carNumber, (Cars car) -> {
            car.setStatus("soldout");
            car.setColor(CommonCodes.Colors.BROWN);
        });
        Cars car = carsRepository.findById(carNumber).get();
        System.out.println(car.getStatus() + ", " + carOrigin.getColor());
    }

    private Cars processSaleCommon(String id, Consumer<Cars> saleCar) {
        Cars car = carsRepository.findById(id).get();
        saleCar.accept(car);
//        carsRepository.save(car);
        Cars modifiedCar = carsJpaRepository.saveAndFlush(car);
        return modifiedCar;
    }

    abstract class SaleCar {
        public void processSale(String carnumber) {
            Cars car = carsRepository.findById(carnumber).get();
            saleCar(car);
        }
        abstract void saleCar(Cars car);
    }
}
