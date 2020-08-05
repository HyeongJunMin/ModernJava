package com.mj.modernjava.ch12;

import java.time.DayOfWeek;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public class TemporalUtil {

  public static Temporal adjustInto(Temporal temporal) {
    DayOfWeek dayOfWeek = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
    int dayToAdd = 1;
    if (dayOfWeek == DayOfWeek.FRIDAY) dayToAdd = 3;
    else if (dayOfWeek == DayOfWeek.SATURDAY) dayToAdd = 2;
    return temporal.plus(dayToAdd, ChronoUnit.DAYS);
  }

}
