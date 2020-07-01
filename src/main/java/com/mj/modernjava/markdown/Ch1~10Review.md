####문제1. 
필터링+그루핑
####문제2.
DB에 있는 차들 중 status가 normal인 모든 차가 팔렸다고 할 때, 브랜드 별 매출 합계 구하기 
####문제3.
Cars의 model이름을 Set<String>으로 매핑해서 Set의 값들을 String.toUppercase()메소드를 사용해서 대문자로 바꾸고자 할 때 병렬처리, 순차처리 속도차이 측정
####문제4.
메소드파라미터화 예제... 보면 볼수록 stream으로 하는게 낫다
- 예시
    -   메소드파라미터화
        -   filter(cars, car -> "HyeonDai".equals(car.getBrand());
        -   filter(cars, car -> "Bentayga".equals(car.getModel());
    -   stream
        -   cars.stream().filter(car -> "HyeonDai".equals(car.getBrand());
        