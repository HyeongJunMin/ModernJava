package com.mj.modernjava.ch7;

import java.util.Spliterator;
import java.util.function.Consumer;

//Spliterator를 구현한 다음 병렬 스트림으로 전달
class  WordCounterSpliterator implements Spliterator<Character> {
  private final String string;
  private int currentChar = 0;
  public WordCounterSpliterator(String string) {
    this.string = string;
  }
  @Override
  public boolean tryAdvance(Consumer<? super Character> action) {
    action.accept(string.charAt(currentChar++));  //현재 문자 소비
    return currentChar < string.length(); //소비할 문자가 남았으면 true
  }
  @Override
  public Spliterator<Character> trySplit() {
    int currentSize = string.length();
    if (currentSize < 10) {
      return null;
    }
    //파싱할 문자열의 중간을 분할위치로 설정
    for (int splitPos = currentSize/2 + currentChar; splitPos < string.length(); splitPos++) {
      if (Character.isWhitespace(string.charAt(splitPos))) {
        //처음부터 분할 위치까지 문자열을 파싱할 새로운 Spliterator 생성
        Spliterator<Character> spliterator = new WordCounterSpliterator(string.substring(currentChar, splitPos));
        currentChar = splitPos; //생성한 spliterator의 시작위치를 분할 위치로 설정
        return spliterator; //공백을 찾았고 문자열을 분리했으므로 루프 종료
      }
    }
    return null;
  }
  @Override
  public long estimateSize() {
    return string.length() - currentChar;
  }
  @Override
  public int characteristics() {
    return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
  }
}
