package com.mj.modernjava.ch21;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Ch21Conclusion {
  @Test
  void generic() {
    List<? extends Number> numberList = new ArrayList<Integer>();
    // List<Number> numbers = new ArrayList<Integer>();
  }
}
