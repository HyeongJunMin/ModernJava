package com.mj.modernjava.review.domain;

import com.mj.modernjava.common.CommonCodes;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private String created_date;
    private String status;

    @Transient
    public boolean isFast() {
        if (this.maxSpeed >= 250) {
            return true;
        }
        return false;
    }

    @Transient
    public boolean isIntervenePrice(Long minPrice, Long maxPrice) {
        return minPrice.longValue() <= this.price.longValue() && this.price.longValue() <= maxPrice.longValue() ? true : false;
    }

    @Transient
    public boolean isNormal() {
        return CommonCodes.CarStatus.NORMAL.equals(this.status);
    }
}
