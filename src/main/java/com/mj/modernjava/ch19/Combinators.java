package com.mj.modernjava.ch19;

import java.util.function.Function;

public class Combinators {
  // 함수 f와 g를 인수로 받아 f의 기능을 적용한 다음 g의 기능을 적용하는 함수를 반환
  public static <A, B, C> Function<A, C> compose(Function<B, C> g, Function<A, B> f) {
    return x -> g.apply(f.apply(x));
  }
  // f에 연속적으로 n번 적용
  public static <A> Function<A, A> repeat(int n, Function<A, A> f) {
    return n == 0 ? x -> x : compose(f, repeat(n - 1, f));
  }
}
