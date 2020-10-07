package com.mj.modernjava.ch19;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class LazyList<T> implements MyList<T>{
  private final T head;
  private final Supplier<MyList<T>> tail;
  public LazyList(T head, Supplier<MyList<T>> tail) {
    this.head = head;
    this.tail = tail;
  }
  public T head() {
    return head;
  }
  public MyList<T> tail() {
    return tail.get();
  }
  public boolean isEmpty() {
    return false;
  }
  public MyList<T> filter(Predicate<T> p) {
    return isEmpty()
            ? this
            : p.test(head())
                ? new LazyList(head(), () -> tail().filter(p))
                : tail().filter(p);
  }
  public static LazyList<Integer> from(int n) {
    return new LazyList<Integer>(n, () -> from(n + 1));
  }
  public static LazyList<Integer> primes(MyList<Integer> numbers) {
    return new LazyList<>(
        numbers.head(),
        () -> primes(
            numbers.tail()
                  .filter(n -> n % numbers.head() != 0)
        )
    );
  }
}
