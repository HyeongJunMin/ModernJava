# 목차

> 요약 및 결론  
> 책 내용  

---

#### 요약 및 결론
- Date 타입명이 참 아깝다... 지금 LocalDateTime을 Date로 바꾸면 참 좋을텐데 아쉽
- LocalDateTime 쓰면 좋긴 할텐데 Date 클래스 쓰던곳은 apache DateUtils 써도 충분할듯..
- timezone 부분은 확실히 편함
---

#### 책 내용
0. 자바8 이전까지 날짜와 시간 API
    - 1.0부터 Date 쓰고있었음
        - 결과가 직관적이지 않음(월 시작이 0 등)
        - toString 문자열을 추가로 활용하기 어려움
    - 1.1에서 Calendar가 나왔지만 혼란을 가중시킴
1. 새로운 날짜와 시간 API
    1. LocalDate와 LocalTime 사용
    2. LocalDateTime(LocalDate + LocalTime)
    ```
    @Test
    public void testLocalDateTime() {
      //확실히 Date 보다 직관적으로 사용하기 편하다
      LocalDate date = LocalDate.of(2020, 8, 5);
      log.info("year : {}, month : {}, day : {}, dayOfWeek : {}, lengthOfMonth : {}"
          , date.getYear(), date.getMonth(), date.getDayOfMonth(), date.getDayOfWeek(), date.lengthOfMonth());
      LocalTime time = LocalTime.of(19, 11, 30);
      log.info("hour : {}, minute : {}, second : {}", time.getHour(), time.getMinute(), time.getSecond());
      // LocalDateTime을 생성하는 여러가지 방법
      List<LocalDateTime> ldtList = new ArrayList();
      ldtList.add(LocalDateTime.now());
      ldtList.add(LocalDateTime.of(2020, Month.AUGUST, 5, 19, 11, 30));// 년 월 일 시 분 초
      ldtList.add(LocalDateTime.of(date, time));
      ldtList.add(date.atTime(19, 11, 30));
      ldtList.add(date.atTime(time));
      ldtList.add(time.atDate(date));
      ldtList.forEach(ldt -> log.info("ldt : {}", ldt));
    }
    ```
    3. Instant 클래스 : 기계의 날짜와 시간
        - Unix Epoch Time을 기준으로 정 지점까지의 시간을 초로 표현
        - 나노초 단위 표현 가능
        - Duration과 Period 클래스를 함께 사용할 수 있다.
    4. Duration과 Period 정의
        - Duration : 두 시간 객체 사이의 지속시간
    ~~~
    @Test
    public void testInstantDurationPeriod() {
      // 기계적인 관점에서 시간을 표현하기 위한 방법
      log.info("instant now : {}", Instant.now());
      log.info("instant : {}", Instant.ofEpochMilli(3));  // instant : 1970-01-01T00:00:00.003Z
      log.info("instant : {}", Instant.ofEpochSecond(3, 1));  // instant : 1970-01-01T00:00:03.000000001Z
      LocalTime time1 = LocalTime.of(15, 0, 0);
      LocalTime time2 = LocalTime.of(16, 30, 0);
      // between정적메소드의 파라미터는 Temporal임. 아주 여러가지의 날짜 시간 관련 클래스들이 구현하고있는 인터페이스. 대부분되는듯
      // LocalDateTime과 Instant는 서로 혼합할 수 없음(예외발생함)
      // Duration은 초와 나노초로 시간단위를 표현하기 때문에 LocalDate 전달 불가
      log.info("Duration Between : {}", Duration.between(time1, time2));  // Duration Between : PT1H30M
      // LocalDate는 Period 사용해야함. Period의 between메소드의 매개변수는 LocalDate
      LocalDate ld1 = LocalDate.now();
      LocalDate ld2 = LocalDate.of(2020, 7, 2); // LocalDate Between : P-1M-3D
      log.info("LocalDate Between : {}", Period.between(ld1, ld2));
      Period period = Period.of(2020, 8, 5);
    }
    ~~~
2. 날짜 조정, 파싱, 포매팅
    - with~ 메소드는 기존 객체는 그대로 두고 갱신된 복사본을 리턴하는 메소드
    - https://docs.oracle.com/javase/8/docs/api/java/time/temporal/TemporalAdjusters.html
    - DateTimeFormatter
        - java.time.format 패키지에서 가장 중요
        - 날짜나 시간을 특정 형식의 문자열로 만들 수 있다.
    ~~~
    @Test
    public void testAdjustParsingFormatting() {
      LocalDate date = LocalDate.of(2020, 8, 5);
      LocalDate date2 = date.with(ChronoField.MONTH_OF_YEAR, 2);
      LocalDate date3 = LocalDate.of(2020, 8, 5);
      LocalDate date4 = date.with(ChronoField.DAY_OF_MONTH, 10);
      log.info("is eqauls? : {}", date.equals(date2));
      log.info("is eqauls? : {}", date.equals(date3));
      log.info("date 4 : {}", date4); // date 4 : 2020-08-10
      // TemporalAdjusters
      log.info("lastDayOfMonth : {}", date.with(lastDayOfMonth())); // lastDayOfMonth : 2020-08-31
      log.info("nextPrevious : {}", date.with(next(DayOfWeek.SATURDAY))); // nextPrevious : 2020-08-08
      // Custom TemporalAdjuster
      log.info("next working day : {}", date.with(new NextWorkingDay())); // next working day : 2020-08-06
      // Custom 할일이 많아지면 이런식으로 UtilClass 선언해서 쓰는 것도 좋은 방법일 듯
      log.info("next working day : {}", date.with(TemporalUtil::adjustInto)); // next working day : 2020-08-06
      // DateTimeFormatter
      log.info("basic iso : {}", date.format(DateTimeFormatter.BASIC_ISO_DATE));  // basic iso : 20200805
      log.info("iso local : {}", date.format(DateTimeFormatter.ISO_LOCAL_DATE));  // iso local : 2020-08-05
      try {
        log.info("iso local : {}", date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)); //Exception
      } catch (UnsupportedTemporalTypeException e) {
        log.error("UnsupportedTemporalTypeException");  // LocalDate에 Time 없으면 알아서 00:00:00 해줄줄 알았는데 아님
      }
      String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
      log.info("string pattern : {}", formattedDate); // string pattern : 05/08/2020
      LocalDate parsedDate = LocalDate.parse(formattedDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
      log.info("parsedDate : {}", parsedDate);  //  parsedDate : 2020-08-05
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREA);
      log.info("dateTimeFormatter chronology : {}, locale : {}, decimalStyle : {}, resolverFields : {}, zone : {}"
          , dtf.getChronology(), dtf.getLocale(), dtf.getDecimalStyle(), dtf.getResolverFields(), dtf.getZone());
      //  DateTimeFormatterBuilder
      DateTimeFormatter dtfItalian = new DateTimeFormatterBuilder()
          .appendText(ChronoField.DAY_OF_MONTH).appendLiteral(". ")
          .appendText(ChronoField.MONTH_OF_YEAR).appendLiteral(" ")
          .appendText(ChronoField.YEAR)
          .parseCaseInsensitive().toFormatter(Locale.ITALIAN);
      String italianFormatted = date.format(dtfItalian);
      log.info("italianFormatted : {}", italianFormatted);  //  italianFormatted : 5. agosto 2020
    }
    ~~~
3. 다양한 시간대와 캘린더 활용 방법
    - TimeZone 다루기
    - 기존 java.util.TimeZone을 대체하는 java.time.ZoneId(불변클래스) 등장
    - 지역ID는 {지역}/{도시} 형식으로 이루어지며 iana.org/time-zones에서 제공하는 지역 집합 정보 사용
    1. 시간대 사용하기(ZoneId)
        ~~~
        @Test
        public void testZoneId() {
          ZoneId rome = ZoneId.of("Europe/Rome");
        
          ZonedDateTime localDate = LocalDate.of(2020, 8, 5).atStartOfDay(rome);
          log.info("localDate : {}", localDate);  //  localDate : 2020-08-05T00:00+02:00[Europe/Rome]
          ZonedDateTime localDateTime = LocalDateTime.now().atZone(rome);
          log.info("localDateTime : {}", localDateTime);  //  localDateTime : 2020-08-05T21:04:18.906+02:00[Europe/Rome]
          ZonedDateTime instant = Instant.now().atZone(rome);
          log.info("instant : {}", instant);    //  instant : 2020-08-05T14:04:18.907+02:00[Europe/Rome]
          LocalDateTime localDateTimeByInstant = LocalDateTime.ofInstant(Instant.now(), rome);
          // Instant는 기존 Date 클래스와 호환성이 좋다
          Date date = new Date();
          date.toInstant();
          date.from(Instant.now());
        }
        ~~~
    2. UTC/GMT 기준의 고정 오프셋
        - UTC : Universal Time Coordinated
        - GMT : Greenwich Mean Time
        ```
        @Test
        public void testOffset() {
          ZoneOffset seoulOffset = ZoneOffset.of("+09:00");
          LocalDateTime localDateTime = LocalDateTime.of(2020, Month.AUGUST, 5, 19, 11, 30);
          OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, seoulOffset);
          log.info("offsetDateTime : {}", offsetDateTime);
          log.info("offset info = rules : {}, toString : {}, totalSeconds : {}"
              , seoulOffset.getRules(), seoulOffset.toString(), seoulOffset.getTotalSeconds());
          // offset info = rules : ZoneRules[currentStandardOffset=+09:00], toString : +09:00, totalSeconds : 32400
        }
        ```
    3. 대안 캘린더 시스템 사용하기
        - ChronoLocalDate를 구현하는 클래스 : 
            - ThaiBuddhistDate : 태국 불교 달력으로, 하루를 4개의 6시간으로 나누는 특징이 있음
            - MinguoDate : 중국이 쓰는 민국달력으로, 서기1912년을 1년으로 환산
            - JapaneseDate : 일본
            - HijrahDate : 이슬람 달력
            - LocalDate : 설계자는 ChronoLocalDate쓰지 말고 LocalDate 사용하라고 권고
        - 갖는 메소드들은 다 비슷해 보이는데 내부 계산에 뭔가 있나봄
        ~~~
        @Test
        public void testHijrahDate() {
          // 현재 HijrahDate를 얻고 그 날짜를Ramadan의 첫 째 날, 즉 9번 째 달로 바꿈
          HijrahDate ramadanDate = HijrahDate.now().with(ChronoField.DAY_OF_MONTH, 1)
                                                    .with(ChronoField.MONTH_OF_YEAR, 9);
          log.info("instance : {}", IsoChronology.INSTANCE.date(ramadanDate));
          log.info("adjuster : {}", IsoChronology.INSTANCE.date(ramadanDate.with(TemporalAdjusters.lastDayOfMonth())));
          // instance : 2020-04-24 adjuster : 2020-05-23
        }
        ~~~
    

#### Locale을 갖는 ZonedDateTime을 다른 TimeZone으로 변환하는 방법
```
@Test
public void testParseTimeToAnotherZone() {
  LocalDateTime ldt = LocalDateTime.now();
  ZonedDateTime seoul = ldt.atZone(ZoneId.of("Asia/Seoul"));
  log.info("seoul : {}", seoul);  //  seoul : 2020-08-05T21:43:10.111+09:00[Asia/Seoul]
  ZonedDateTime montreal = seoul.withZoneSameInstant(ZoneId.of("America/Montreal"));
  log.info("montreal : {}", montreal);  //  2020-08-05T08:43:10.111-04:00[America/Montreal]
}
```

#### apache.commons.lang3.time.DateUtils
DateUtils 쓰면 기존에 있던 Date도 쓸만 함
```
@Test
public void testDate1() {
  // 1501513200000
  log.debug("" + DateUtil.convertDateToString("yyyyMMdd", DateUtil.convertTimestampToDate(3586950000000L)));
  log.debug(DateUtil.getDate().getTime() + "");
  log.debug(DateUtil.addDays(7).getTime() + "");
  log.debug(DateUtil.addDays(10).getTime() + "");
}
```
```
@Test
public void testDate2() {
  Date targetDt = new Date();
  Date firstDateOfPrevMonth = DateUtils.getFirstDateOfMonth(targetDt, "Asia/Seoul");
  Date endDateOfPrevMonth = DateUtils.getLastDateOfMonth(targetDt, "Asia/Seoul");
}
```