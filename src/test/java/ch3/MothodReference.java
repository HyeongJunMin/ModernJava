package ch3;

import common.Apple;
import common.Item;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

public class MothodReference {

  @Test
  public void methodRef1() {
    Map<String, Apple> map = new HashMap<>();
    Supplier<Apple> sp = Apple::new;
    sp.get();
    
    Function<Integer, Integer> f = x -> x + 1;
    Function<Integer, Integer> g = x -> x * 2;
    Function<Integer, Integer> h = f.andThen(g);
    int result = h.apply(1); //result == 4
  }
}
