package com.mj.modernjava.ch7;

import java.util.ArrayList;
import java.util.Spliterator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ParallelTest {

  @Test
  public void forkJoinSumTest() {
    //ForkJoinSumCalculator에서 정의한 태스크를 수행하고 결과를 출력하는 테스트
    int n = 1000;
    long[] numbers = LongStream.rangeClosed(1, n).toArray();
    ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);
    Long invoke = (Long) (new ForkJoinPool().invoke(task));
    log.debug("invoke : {}", invoke);

    ArrayList<Object> list = new ArrayList<>();
    int characteristics = list.spliterator().characteristics();
  }

  @Test
  public void customSpliteratorTest() {

  }

  //반복형으로 단어 수를 세는 메소드
  //얘가 Spliterator랑 무슨상관?
  private int countWordsIteratively(String s) {
    int counter = 0;
    boolean lastSpace = true;
    for (char c : s.toCharArray()) {
      if (Character.isWhitespace(c)) {
        lastSpace = true;
      } else {
        if (lastSpace) {
          counter++;
        }
        lastSpace = false;
      }
    }
    return counter;
  }

  //문자열 스트림을 탐색하면서 단어 수를 세는 클래스
  public class WordCounter {
    private final int counter;
    private final boolean lastSpace;
    public WordCounter(int counter, boolean lastSpace) {
      this.counter = counter;
      this.lastSpace = lastSpace;
    }
    public WordCounter accumulate(Character c) {
      if (Character.isWhitespace(c)) {
        return lastSpace ? this : new WordCounter(counter, true);
      } else {
        return lastSpace ? new WordCounter(counter + 1, false) : this;
      }
    }
    public WordCounter combine(WordCounter wordCounter) {
      return new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace);
    }
    public int getCounter() {
      return counter;
    }
  }
  //문자 스트림의 리듀싱 연산 구현
  private int countWords(Stream<Character> stream) {
    WordCounter wordCounter = stream.reduce(new WordCounter(0, true), WordCounter::accumulate, WordCounter::combine);
    return wordCounter.getCounter();
  }

  //WordCounterSpliterator 활용
  @Test
  public void wordCounterSpliteratorTest() {
    Spliterator<Character> spliterator = new WordCounterSpliterator("Hello World");
    Stream<Character> stream = StreamSupport.stream(spliterator, true);
    log.debug(String.format("Found %d words", countWords(stream)));
    //결과 : Found 19 words
  }

}
