package com.mj.modernjava.ch3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ExcuteAroundPatternByLambda {

  //실행 어라운드 패턴을 적용하는 네 단계의 과정

  //1
  public String processFile() throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
      return br.readLine();
    }
  }

  //2
  public interface BufferedReaderProcessor {
    String process(BufferedReader br) throws IOException;
  }

  //3
  public String processFile(BufferedReaderProcessor p) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
      return p.process(br);
    }
  }

  //4
  public void runWithLambda() throws IOException{
      String oneLine = processFile((BufferedReader br) -> br.readLine());
      String twoLines = processFile((BufferedReader br) -> br.readLine() + br.readLine());
  }


}
