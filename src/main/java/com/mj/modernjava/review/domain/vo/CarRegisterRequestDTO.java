package com.mj.modernjava.review.domain.vo;

import java.util.Date;

public class CarRegisterRequestDTO {
    private String carNumber;
    private String color;
    private String brand;
    private String model;
    private Integer maxSpeed;
    private Integer distance;
    private Integer height;
    private Integer width;
    private Integer depth;
    private Integer maxFuel;
    private Integer fuel;
    private Long price;
    private Integer since;
    private String carType;
    private Date created_dt;
    private String status;

    public boolean isValidCarInfo() {
      return true;
    }
}
