<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Title</title>
  <!-- Bootstrap, jQuery CDN -->
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"/>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
  <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet"/>


</head>
<body>
<div id="carWrap" style="width: 80%; margin: auto;">
  <ul class="nav nav-tabs" id="carMainTab" role="tablist">
    <li class="nav-item">
      <a class="nav-link active" id="recommend-tab" data-toggle="tab" href="#carRecommendationForm" role="tab" aria-controls="recommend" aria-selected="true">차량추천</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" id="sales-tab" data-toggle="tab" href="#carSalesForm" role="tab" aria-controls="sales" aria-selected="false">판매량</a>
    </li>
  </ul>
  <div class="tab-content" id="carMainTabContent">
    <div class="tab-pane fade show active" id="carRecommendationForm" role="tabpanel" aria-labelledby="recommend-tab">
      <div class="row">
        <div class="col-md-6 col-sm-6 col-xs-12 form-set">
          <label>최저속도</label>
          <input class="form-control" type="text" id="minSpeed" placeholder="최저속도(0~300km/h)" value="250">
          <label>최저가격</label>
          <input class="form-control" type="text" id="minPrice" placeholder="최저가격" value="250000000">
        </div>
        <div class="col-md-6 col-sm-6 col-xs-12 form-set">
          <label>최고가격</label>
          <input class="form-control" type="text" id="maxPrice" placeholder="최고가격" value="300000000">
          <label>추천 수</label>
          <select class="form-control" id="recommendLimit" value="3">
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3" selected>3</option>
          </select>
        </div>
        <div class="row" style="margin:auto; margin-top:10px;">
          <div class="col-md-12 col-sm-12 col-xs-12 form-set">
            <input type="button" class="btn btn-primary" value="추천" id="submitRecommend">
          </div>
        </div>

        <div id="recommendResult" class="row col-md-12 col-sm-12 col-xs-12 form-set">
          <table class="table table-hover" style="display:none;">
            <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">브랜드</th>
              <th scope="col">모델</th>
              <th scope="col">최고속도</th>
              <th scope="col">가격</th>
              <th scope="col">색상</th>
            </tr>
            </thead>
            <tbody>
              <tr>

              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <div class="tab-pane fade" id="carSalesForm" role="tabpanel" aria-labelledby="sales-tab">
      <div class="row">
        <div class="col-md-12 col-sm-12 col-xs-12 form-set">
          <label>판매량</label>
          <input id="btnGetSalesTotal" class="btn btn-primary" type="button" name="getSalesTotal" value="확인">
        </div>
      </div>
      <div class="row">
        <div class="col-md-12 col-sm-12 col-xs-12 form-set">
          <div id="salesResult">

          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<script>
$(document).ready(function() {
  $('#submitRecommend').click(function () {
    let minSpeed = $('#minSpeed').val().trim();
    let minPrice = $('#minPrice').val().trim();
    let maxPrice = $('#maxPrice').val().trim();
    let recommendLimit = $('#recommendLimit').val().trim();
    console.log('submitRecommend click');
    $('#recommendResult').remove('tr');
    $.ajax({
      method: "POST",
      url: "/car/recommend",
      data: { minSpeed: minSpeed, minPrice: minPrice, maxPrice: maxPrice, recommendLimit: recommendLimit }
    })
    .done(function( msg ) {
      console.log( "Data Saved: " + JSON.stringify(msg) );
      $.each(msg, function(k, v) {
        console.log(k);
        console.log(JSON.stringify(v));
        $('#recommendResult').append('<p>' + JSON.stringify(k) + '<p>');
        $('#recommendResult').append('<p>' + JSON.stringify(v) + '<p>');
      })
    });
  });

  $('#btnGetSalesTotal').click(function () {
    $.ajax({
      method: "POST",
      url: "/car/sales/total"
    })
    .done(function( msg ) {
      $.each(msg, function(k, v) {
        console.log(k);
        console.log(JSON.stringify(v));
        $('#salesResult').append('<p> 브랜드 : ' + JSON.stringify(k) + ', 판매금액 : ' + numberWithCommas(JSON.stringify(v)) + '<p>');
      })
    });
  })
});
function numberWithCommas(x) {
  return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}
</script>
</body>
</html>