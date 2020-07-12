package com.mj.modernjava.review.controller;

import com.mj.modernjava.review.domain.Cars;
import com.mj.modernjava.review.domain.vo.DemandVO;
import com.mj.modernjava.review.service.CarsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/car")
@RequiredArgsConstructor
public class CarViewController {

    private final CarsManager carsManager;

    @GetMapping("/")
    public String showCarMain() {
        return "/car/carmain";
    }

    @PostMapping("/recommend")
    @ResponseBody
    public Map<String, List<Cars>> recommendCar(DemandVO demandVO) {
        log.info("req demand : {}", demandVO);
        return carsManager.recommendedCarsBy(demandVO);
    }

    @PostMapping("/sales/total")
    @ResponseBody
    public Map<String, Long> carSalesTotal() {
        return carsManager.getSalesTotal();
    }
}
