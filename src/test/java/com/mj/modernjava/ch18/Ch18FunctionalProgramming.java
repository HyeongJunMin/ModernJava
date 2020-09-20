package com.mj.modernjava.ch18;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Ch18FunctionalProgramming {

  @Test
  void divideList() {
    List<Integer> originList = Arrays.asList(1, 4, 9);
    List<List<Integer>> subList = subsets(originList);
    subList.forEach(list -> log.info("list : {}", list));
    // list subsets : [1, 4, 9]
    // list subsets : [4, 9]
    // list subsets : [9]
    // list subsets : []
    // sub result : [[]]
    // insertAll progress. firstInteger : 9, list : [[]]
    // concat a : [[]], b : [[9]]
    // sub result : [[], [9]]
    // insertAll progress. firstInteger : 4, list : [[], [9]]
    // concat a : [[], [9]], b : [[4], [4, 9]]
    // sub result : [[], [9], [4], [4, 9]]
    // insertAll progress. firstInteger : 1, list : [[], [9], [4], [4, 9]]
    // concat a : [[], [9], [4], [4, 9]], b : [[1], [1, 9], [1, 4], [1, 4, 9]]
    // list : []
    // list : [9]
    // list : [4]
    // list : [4, 9]
    // list : [1]
    // list : [1, 9]
    // list : [1, 4]
    // list : [1, 4, 9]
  }

  // 부분집합을 가른다
  private static List<List<Integer>> subsets(List<Integer> list) {
    log.info("list subsets : {}", list);
    if (list.isEmpty()) {
      List<List<Integer>> emptyResult = new ArrayList<>();
      emptyResult.add(Collections.emptyList());
      return emptyResult;
    }
    Integer firstInteger = list.get(0);
    List<Integer> rest = list.subList(1, list.size());
    // 빈 리스트가 아니면 먼저 하나의 요소를 꺼내고
    // 나머지 요소의 서브집합을 찾아서 subResult로 전달한다.
    // subResult는 절반의 정답을 포함한다.
    List<List<Integer>> subResult = subsets(rest);
    log.info("sub result : {}", subResult);
    // 처음 꺼내놨던 요소를 subResult의 요소마다 firstInteger를 하나씩 붙인 요소들을 포함한다.
    List<List<Integer>> subResultFirstIntegerAdded = insertAll(firstInteger, subResult);
    // 두 subResult를 합친다.
    return concat(subResult, subResultFirstIntegerAdded);
  }

  public static <T> List<List<T>> insertAll(T firstInteger, List<List<T>> lists) {
    log.info("insertAll progress. firstInteger : {}, list : {}", firstInteger, lists);
    List<List<T>> result = new ArrayList<>();
    for (List<T> list : lists) {
      List<T> copyList = new ArrayList<>();
      copyList.add(firstInteger);
      copyList.addAll(list);
      result.add(copyList);
    }
    return result;
  }

  static <T> List<List<T>> concat(List<List<T>> a, List<List<T>> b) {
    log.info("concat a : {}, b : {}", a, b);
    List<List<T>> r = new ArrayList<>(a);
    r.addAll(b);
    return r;
  }

  // 반복 방식의 팩토리얼과 재귀 방식, 그리고 스트림 방식 팩토리얼
  @Test
  void factorials() {
    log.info("iterative : {}", factorialIterative(10));
    log.info("recursive : {}", factorialRecursive(10L));
    log.info("stream : {}", factorialStreams(10L));
  }

  private int factorialIterative(int n) {
    int r = 1;
    for (int i = 1; i <= n; i++) {
      r *= i;
    }
    return r;
  }

  private long factorialRecursive(long n) {
    return n == 1 ? 1 : n * factorialRecursive(n - 1);
  }

  private long factorialStreams(long n) {
    return LongStream.rangeClosed(1, n)
            .reduce(1, (long a, long b) -> a * b);
  }

}
