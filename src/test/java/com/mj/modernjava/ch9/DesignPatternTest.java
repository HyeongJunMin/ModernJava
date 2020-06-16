package com.mj.modernjava.ch9;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class DesignPatternTest {

  @Test
  public void strategyPatternTest() {
    Validator numericValidator = new Validator(new IsNumeric());
    boolean isNumeric = numericValidator.validate("aaaa");
    log.debug("is numeric : {}", isNumeric);

    Validator caseValidator = new Validator(new IsAllLowerCase());
    boolean isLower = numericValidator.validate("aaaa");
    log.debug("is lower case : {}", isLower);
  }

}
