package com.mj.modernjava.review.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class Cars {
    @Id
    @Column(name = "carnumber")
    private String carNumber;

    private String color;
    private String brand;
    private String model;

    @Column(name = "max_speed")
    private Integer maxSpeed;
    private Integer distance;
    private Integer height;
    private Integer width;
    private Integer depth;
    @Column(name = "max_fuel")
    private Integer maxFuel;
    private Integer fuel;
    private Long price;
    private Integer since;
    @Column(name = "car_type")
    private String carType;
    private Date created_dt;
    private String status;

}
