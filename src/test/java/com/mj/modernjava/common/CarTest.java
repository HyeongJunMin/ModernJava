package com.mj.modernjava.common;

import com.mj.modernjava.review.controller.CarStatusController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CarTest {

//    @Autowired
//    private CarsRepository carsRepository;

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
}
