package com.mj.modernjava.common;

import com.mj.modernjava.review.controller.CarStatusController;
import com.mj.modernjava.review.domain.Cars;
import com.mj.modernjava.review.domain.vo.DemandVO;
import com.mj.modernjava.review.repository.CarsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CarTest {

    @Autowired
    private CarsRepository carsRepository;

    @Autowired
    private CarStatusController carStatusController;

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    void carConnectionTest() {
//        log.info("test start");
//        Optional<Cars> byId = carsRepository.findById("00하3929");
//        log.info("car : {}", byId.get().toString());
//        log.info("car number : {}", byId.get().getCarNumber());
//        carsRepository.findAll().forEach(car -> log.info("car : {}", car));
//    }

    @Test
    void carControllerConnectionTest() throws Exception{
        mockMvc.perform(
                get("/car/connection")
        ).andDo(print());
    }

    @Test
    void carUpdateTest() throws Exception{

        mockMvc.perform(
                post("/car/update")
                .param("carNumber", "52모3156")
                .param("status", "stolen")
        ).andDo(print());
    }

    @Test
    void carRecommendation() {
        //BOOK 219 PAGE
        ArrayList<Cars> cars = Lists.newArrayList(carsRepository.findAll());
        Map<String, List<Cars>> collect = cars.stream().collect(groupingBy(Cars::getModel));
        log.info("===========");
        log.info("collect : {}", collect);
        log.info("===========");
        DemandVO demand = DemandVO.builder()
                .minSpeed(250L).maxPrice(250_000_000L).minPrice(300_000_000L).recommendLimit(3).build();
        log.info("test start");
        carsRepository.findAll();
        Map<String, List<Cars>> carList = Lists.newArrayList(carsRepository.findAll())
                .stream()
//                .peek(System.out::println)
//                .filter(Cars::isFast)
                .filter(car -> car.getPrice() >= demand.getMinPrice() && car.getPrice() <= demand.getMaxPrice())
//                .distinct()
//                .limit(demand.getRecommendLimit())
                .collect(groupingBy(Cars::getModel));

        log.info("car list size : {}", carList.size());
        carList.forEach((k, v) ->log.info("info : " + k + v));
    }

    @Test
    void functionTest() {
        Optional<Cars> byId = carsRepository.findById("00하3929");
        calculateDistancePerLiter(byId.get(), (Cars car) -> car.getDistance() / car.getFuel());
    }

    private void calculateDistancePerLiter(Cars car, Function<Cars, Integer> f) {
        Integer apply = f.apply(car);
        System.out.println(apply);
    }

    @Test
    void behaviorParameterizationTest() {
        ArrayList<Cars> cars = Lists.newArrayList(carsRepository.findAll());

    }

    @FunctionalInterface
    public interface UpdateCar<T> {
        //the abstract method
        public void singleMethod(T param);
    }

    @Test
    void parallelPerformance() {
        Set<String> modelNameSet = Sets.newHashSet(carsRepository.findAll()).stream().map(Cars::getModel).collect(toSet());
//        modelNameSet.forEach(model -> System.out.println("model : " + model));
        for(int i = 0 ; i < 100; i++ ){
            modelNameSet.stream().forEach(name -> {
                modelNameSet.add(name + name);
            });
        }

        System.out.println("-------------------");
        System.out.println("modelname size : " + modelNameSet.size());
        System.out.println("-------------------");
        StopWatch stopWatch = new StopWatch();
        IntStream.range(0, 10).forEach(i -> {
            stopWatch.reset();
            stopWatch.start();
            modelNameSet.stream().map(model -> model.toUpperCase());
            stopWatch.stop();
            System.out.println(i + " time : " + stopWatch.getNanoTime());
        });


        stopWatch.reset();
        stopWatch.start();
        modelNameSet.parallelStream().map(model -> model.toUpperCase());
        stopWatch.stop();
        System.out.println("time : " + stopWatch.getNanoTime());


    }

    @Test
    void calculateTotalSales() {
        //문제 2
        Map<String, Long> collect = Lists.newArrayList(carsRepository.findAll())
                .stream()
                .filter(car -> "normal".equals(car.getStatus()))
                .collect(groupingBy(Cars::getBrand, summingLong(Cars::getPrice)));
        collect.forEach((k, v) -> {
            System.out.println("k : " + k + " , v : " + v);
        });
    }

}
