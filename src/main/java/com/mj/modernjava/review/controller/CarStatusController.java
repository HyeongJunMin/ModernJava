package com.mj.modernjava.review.controller;

import com.mj.modernjava.review.domain.vo.CarRegisterRequestDTO;
import com.mj.modernjava.review.domain.vo.CarUpdateRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CarStatusController {

    @GetMapping("/car/connection")
    public String connection() {
        log.info("connection ok");
        return "ok";
    }

    @PostMapping("/car/update")
    public String updateCarStatus(CarUpdateRequestDTO requestDTO) {
        log.info("dto : {}", requestDTO);
        return "";
    }

    @PostMapping("/car/register")
    public String registerCar(CarRegisterRequestDTO requestDTO) {

        return "";
    }
}
