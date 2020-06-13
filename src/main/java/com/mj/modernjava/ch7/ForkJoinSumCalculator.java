package com.mj.modernjava.ch7;

//RecursiveTask 정의와 추상메소드 compute() 구현
public class ForkJoinSumCalculator extends java.util.concurrent.RecursiveTask<Long>{
  private final long[] numbers;
  private final int start;
  private final int end;
  public static final long THRESHHOLD = 10000L; //이 값 이하의 서브태스크는 더이상 분해 불가
  //메인태스크를 생성할 때 사용할 공개 생성자
  public ForkJoinSumCalculator(long[] numbers) {
      this(numbers, 0, numbers.length);
  }
  //메인태스크의 서브태스크를 재귀적으로 만들 때 사용할 비공개 생성자
  private ForkJoinSumCalculator(long[] numbers, int start, int end) {
    this.numbers = numbers;
    this.start = start;
    this.end = end;
  }
  @Override
  protected Long compute() {
    //태스크에서 더할 배열의 길이
    int length = end - start;
    //기준값과 같거나 작으면 순차적으로 결과를 계산
    if (length <= THRESHHOLD) {
      return computeSequentially();
    }
    //배열의 첫 번째 절반을(왼쪽) 더하는 서브태스크 생성
    ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length/2);
    //ForkJoinPool의 다른 스레드를 통해 새로 생성한 태스크를 비동기 실행
    leftTask.fork();
    //배열의 나머지 반을 다루는 서브태스크
    ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start, start + length/2);
    //두 번째 서브태스크 동기 실행. 추가로 분할이 발생할 수 있음
    Long rightResult = rightTask.compute();
    //첫 번째 서브태스크의 결과를 읽거나 아직 결과가 없으면 대기
    Long leftResult = leftTask.join();
    //두 서브태스크의 결과를 조합한 값을 결과로 리턴
    return rightResult + leftResult;
  }
  //더 이상 분할할 수 없을 때 서브태스크의 결과를 계산
  private long computeSequentially() {
    long sum = 0;
    for (int i = start; i < end; i++) {
      sum += numbers[i];
    }
    return sum;
  }
}
